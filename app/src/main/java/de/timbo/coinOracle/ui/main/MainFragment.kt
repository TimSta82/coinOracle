package de.timbo.coinOracle.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentMainBinding
import de.timbo.coinOracle.extensions.showSnackBar
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.CameraUtils
import de.timbo.coinOracle.utils.ImageUtils
import de.timbo.coinOracle.utils.viewBinding
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private companion object {
        const val PHOTO_FILENAME = "exampleFilename"
    }

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val mainViewModel by viewModels<MainFragmentViewModel>()

    private val cameraUtils by inject<CameraUtils>()
    private val imageUtils by inject<ImageUtils>()

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(), ::takePhoto)
    private val takePhotoResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::handlePhoto)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setClickListeners()
    }

    private fun setObservers() {
        mainViewModel.counter.observe(viewLifecycleOwner, ::setCounter)
    }

    private fun setCounter(value: Int) {
        binding.welcomeTv.text = value.toString()
    }

    private fun setClickListeners() {
        binding.counterBtn.setOnClickListener { incrementCounter() }
        binding.takePhotoBtn.setOnClickListener { checkForPermissionAndTakePhoto() }
    }

    private fun incrementCounter() = mainViewModel.onIncrementCounter()

    private fun checkForPermissionAndTakePhoto() {
        if (cameraUtils.checkCameraAvailable(requireContext())) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                takePhoto()
            }
        } else {
            showSnackBar("Keine Kamera im Handy? Wasn das fürn Gerät?")
        }
    }

    private fun takePhoto(isGranted: Boolean = true) {
        if (isGranted) cameraUtils.takePhoto(this, PHOTO_FILENAME, takePhotoResultLauncher)
    }

    private fun handlePhoto(activityResult: ActivityResult) {
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val processedBitmap = imageUtils.getProcessedBitmap(requireContext(), PHOTO_FILENAME)
        }
    }
}
