package de.timbo.coinOracle.ui.assets.assetdetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentAssetDetailsBinding
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

class AssetDetailsFragment : BaseFragment(R.layout.fragment_asset_details) {

    private val binding by viewBinding(FragmentAssetDetailsBinding::bind)
    private val navArgs by navArgs<AssetDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.assetDetailsTv.text = navArgs.assetDetails.asset.name
        binding.assetDetailsHistoryTv.text = navArgs.assetDetails.assetHistory.toString()
    }
}