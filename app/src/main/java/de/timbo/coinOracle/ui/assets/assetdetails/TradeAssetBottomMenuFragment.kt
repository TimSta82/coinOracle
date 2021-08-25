package de.timbo.coinOracle.ui.assets.assetdetails

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentTradeAssetBottomMenuBinding
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.TradingType
import de.timbo.coinOracle.ui.base.BaseBottomSheetDialogFragment
import de.timbo.coinOracle.utils.viewBinding

class TradeAssetBottomMenuFragment(private val asset: Asset) : BaseBottomSheetDialogFragment(R.layout.fragment_trade_asset_bottom_menu) {

    companion object {
        fun createInstance(asset: Asset) = TradeAssetBottomMenuFragment(asset)
    }

    private val binding by viewBinding(FragmentTradeAssetBottomMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.tradeBottomMenuBuyBtn.setOnClickListener {
            findNavController().navigate(AssetDetailsFragmentDirections.actionAssetDetailsFragmentToTradeAssetFragment(asset, TradingType.BUY))
            onDismiss(requireDialog())
        }
        binding.tradeBottomMenuSellBtn.setOnClickListener {
            findNavController().navigate(AssetDetailsFragmentDirections.actionAssetDetailsFragmentToTradeAssetFragment(asset, TradingType.SELL))
            onDismiss(requireDialog())
        }
        binding.tradeBottomMenuConvertBtn.setOnClickListener {
            findNavController().navigate(AssetDetailsFragmentDirections.actionAssetDetailsFragmentToTradeAssetFragment(asset, TradingType.CONVERT))
            onDismiss(requireDialog())
        }
    }
}
