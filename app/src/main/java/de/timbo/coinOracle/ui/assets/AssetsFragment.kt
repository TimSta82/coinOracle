package de.timbo.coinOracle.ui.assets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentAssetsBinding
import de.timbo.coinOracle.extensions.showSnackBar
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

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