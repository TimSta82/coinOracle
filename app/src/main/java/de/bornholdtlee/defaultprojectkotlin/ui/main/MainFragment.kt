package de.bornholdtlee.defaultprojectkotlin.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.database.model.QuestionEntity
import de.bornholdtlee.defaultprojectkotlin.databinding.FragmentMainBinding
import de.bornholdtlee.defaultprojectkotlin.extensions.showSnackBar
import de.bornholdtlee.defaultprojectkotlin.ui.BaseFragment
import de.bornholdtlee.defaultprojectkotlin.utils.CameraUtils
import de.bornholdtlee.defaultprojectkotlin.utils.ImageUtils
import de.bornholdtlee.defaultprojectkotlin.utils.viewBinding
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private companion object {
        const val PHOTO_FILENAME = "exampleFilename"
    }

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val mainViewModel by viewModels<MainViewModel>()

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
        mainViewModel.downloadError.observe(viewLifecycleOwner, ::showSnackBar)
        mainViewModel.questionEntityLiveData.observe(viewLifecycleOwner, ::showQuestionResult)
        mainViewModel.downloadSuccess.observe(viewLifecycleOwner, { showSnackBar(R.string.download_success) })
    }

    private fun setCounter(value: Int) {
        binding.welcomeTv.text = value.toString()
    }

    private fun showQuestionResult(questionEntityList: List<QuestionEntity>) {
        binding.questionsIv.text = questionEntityList.toString()
    }

    private fun setClickListeners() {
        binding.counterBtn.setOnClickListener { incrementCounter() }
        binding.loadQuestionsBtn.setOnClickListener { refreshQuestionsFromApi() }
        binding.takePhotoBtn.setOnClickListener { checkForPermissionAndTakePhoto() }
    }

    private fun refreshQuestionsFromApi() = mainViewModel.makeApiCall()
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
