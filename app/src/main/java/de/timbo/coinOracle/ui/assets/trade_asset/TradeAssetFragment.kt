package de.timbo.coinOracle.ui.assets.trade_asset

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.databinding.FragmentTradeAssetBinding
import de.timbo.coinOracle.extensions.getColorStateListOneColor
import de.timbo.coinOracle.extensions.showSnackBar
import de.timbo.coinOracle.extensions.viewModelsFactory
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.TradePreview
import de.timbo.coinOracle.model.TradingType
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

class TradeAssetFragment : BaseFragment(R.layout.fragment_trade_asset) {

    private val binding by viewBinding(FragmentTradeAssetBinding::bind)
    private val args by navArgs<TradeAssetFragmentArgs>()
    private val viewModel by viewModelsFactory { TradeAssetViewModel(args.asset, args.tradingType) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setObservers()
        setEditTextViewListeners()
    }

    private fun setEditTextViewListeners() {
        binding.tradeAssetAmountEt.doAfterTextChanged { viewModel.calculatePreviewValues(binding.tradeAssetAmountEt.text) }
    }

    private fun setObservers() {
        viewModel.currentAsset.observe(viewLifecycleOwner, ::setCurrentAsset)
        viewModel.tradingType.observe(viewLifecycleOwner, ::initViewWithTradingType)
        viewModel.currentAssetAmount.observe(viewLifecycleOwner, ::setCurrentAssetAmount)
        viewModel.previewValues.observe(viewLifecycleOwner, ::setPreviewValues)
        viewModel.portfolio.observe(viewLifecycleOwner, ::setPortfolio)
        viewModel.onNotEnoughBudget.observe(viewLifecycleOwner, ::showErrorIndicator)
        viewModel.onNotEnoughAssetAmount.observe(viewLifecycleOwner, ::showErrorIndicator)
        viewModel.onSuccess.observe(viewLifecycleOwner) { findNavController().navigateUp() }
        viewModel.onFailure.observe(viewLifecycleOwner) { showSnackBar("Hat nicht geklappt") }
    }

    private fun initViewWithTradingType(tradingType: TradingType) {
        when (tradingType) {
            TradingType.BUY -> binding.tradeAssetConfirmBtn.text = "Kaufen"
            TradingType.SELL -> binding.tradeAssetConfirmBtn.text = "Verkaufen"
            TradingType.CONVERT -> binding.tradeAssetConfirmBtn.text = "Konvertieren"
        }
    }

    private fun showErrorIndicator(hasError: Boolean) {
        binding.tradeAssetPreviewContainer.setStrokeColor(requireContext().getColorStateListOneColor(if (hasError) R.color.red else R.color.green))
        binding.tradeAssetPreviewContainer.strokeWidth = 8
    }

    private fun setPreviewValues(tradePreview: TradePreview) {
        tradePreview.let {
            binding.tradeAssetPreviewContainer.isVisible = true
            binding.tradeAssetPreviewAmountTv.text = "neue Anzahl: ${it.newAmount}"
            binding.tradeAssetPreviewCostTv.text = "kosten: ${it.totalPrice}€"
            binding.tradeAssetPreviewNewSaldoTv.text = "neuer Saldo: ${it.newBudget}€"
            binding.tradeAssetPreviewNewTotalAmountTv.text = "neuer Bestand: ${it.totalAmount}"
        }
    }

    private fun setCurrentAssetAmount(amount: Double) {
        binding.tradeAssetInStockTv.text = "current amount: $amount"
    }

    private fun setPortfolio(portfolioEntity: PortfolioEntity) {
        binding.tradeAssetPortfolioTv.text = "available budget: ${portfolioEntity.budget}€"
        viewModel.initAmount(portfolioEntity)
    }

    private fun setCurrentAsset(asset: Asset) {
        binding.tradeAssetNameTv.text = asset.name
        binding.tradeAssetCurrentPriceTv.text = "Current price: ${asset.priceEuro}€"
    }

    private fun setClickListeners() {
        binding.tradeAssetConfirmBtn.setOnClickListener { viewModel.confirmTrade(args.tradingType) }
    }
}
