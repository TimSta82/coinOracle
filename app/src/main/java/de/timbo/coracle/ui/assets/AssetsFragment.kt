package de.timbo.coracle.ui.assets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import de.timbo.coracle.R
import de.timbo.coracle.databinding.FragmentAssetsBinding
import de.timbo.coracle.extensions.showSnackBar
import de.timbo.coracle.model.Asset
import de.timbo.coracle.ui.BaseFragment
import de.timbo.coracle.utils.viewBinding

class AssetsFragment : BaseFragment(R.layout.fragment_assets) {

    private val binding by viewBinding(FragmentAssetsBinding::bind)
    private val viewModel by viewModels<AssetsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getAssets(euro)
        viewModel.getEuroRate()
        setObservers()
    }

    private fun setObservers() {
        viewModel.assets.observe(viewLifecycleOwner, ::setAssets)
        viewModel.assetsFailure.observe(viewLifecycleOwner) { showSnackBar("loading assets failed") }
    }

    private fun setAssets(assets: List<Asset>) {
        binding.assetsRv.adapter = AssetsAdapter(assets)
    }
}