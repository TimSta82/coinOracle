package de.timbo.coinOracle.ui.assets.trade_asset

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.databinding.FragmentTradeAssetBinding
import de.timbo.coinOracle.extensions.viewModelsFactory
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.TradePreview
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.viewBinding

class TradeAssetFragment : BaseFragment(R.layout.fragment_trade_asset) {

    private val binding by viewBinding(FragmentTradeAssetBinding::bind)
    private val args by navArgs<TradeAssetFragmentArgs>()
    private val viewModel by viewModelsFactory { TradeAssetViewModel(args.asset) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.debug("assetId: ${args.asset}")
        setClickListeners()
        setObservers()
        setEditTextViewListeners()

    }

    private fun setEditTextViewListeners() {
        binding.tradeAssetAmountEt.doAfterTextChanged { viewModel.calculatePreviewValues(binding.tradeAssetAmountEt.text.toString()) }
    }

    private fun setObservers() {
        viewModel.currentAsset.observe(viewLifecycleOwner, ::setCurrentAsset)
        viewModel.currentAssetAmount.observe(viewLifecycleOwner, ::setCurrentAssetAmount)
        viewModel.previewValues.observe(viewLifecycleOwner, ::setPreviewValues)
        viewModel.portfolio.observe(viewLifecycleOwner, ::setPortfolio)
    }

    private fun setPreviewValues(tradePreview: TradePreview) {
        tradePreview.let {
            binding.tradeAssetPreviewContainer.isVisible = true
            binding.tradeAssetPreviewAmountTv.text = "neue Anzahl: ${it.newAmount}"
            binding.tradeAssetPreviewCostTv.text ="kosten: ${it.totalPrice}€"
            binding.tradeAssetPreviewNewSaldoTv.text = "neuer Saldo: ${it.newBudget}€"
            binding.tradeAssetPreviewNewTotalAmountTv.text = "neuer Bestand: ${it.totalAmount}"
        }
    }

    private fun setCurrentAssetAmount(amount: Double) {
        binding.tradeAssetInStockTv.text = "current amount: $amount"
    }

    private fun setPortfolio(portfolioEntity: PortfolioEntity) {
        binding.tradeAssetPortfolioTv.text = "available budget: ${portfolioEntity.budget}€"
    }

    private fun setCurrentAsset(asset: Asset) {
        binding.tradeAssetNameTv.text = asset.name
    }

    private fun setClickListeners() {
        binding.tradeAssetConfirmBtn.setOnClickListener { Toast.makeText(requireContext(), "${args.asset.id}", Toast.LENGTH_SHORT).show() }
    }
}
