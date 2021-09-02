package de.timbo.coinOracle.ui.trading_overview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.databinding.FragmentTradingOverviewBinding
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

class TradingOverviewFragment : BaseFragment(R.layout.fragment_trading_overview) {

    private val viewModel by viewModels<TradingOverviewViewModel>()
    private val binding by viewBinding(FragmentTradingOverviewBinding::bind)

    private val tradesAdapter by lazy { TradingOverviewAdapter(::onTradeClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        initRecycler()
        // TODO implement switch
        setRadioButtonClicklisteners()
    }

    private fun initRecycler() {
        binding.tradingRv.adapter = tradesAdapter
    }

    private fun setRadioButtonClicklisteners() {
        binding.purchaseRb.setOnCheckedChangeListener { _, isChecked ->
            binding.purchaseRb.isChecked = isChecked
            if (isChecked) binding.soldRb.isChecked = isChecked.not()
            viewModel.applyFilterOption(if (isChecked && binding.soldRb.isChecked.not()) FilterOption.PURCHASED else FilterOption.NONE)
        }
        binding.soldRb.setOnCheckedChangeListener { _, isChecked ->
            binding.soldRb.isChecked = isChecked
            if (isChecked) binding.purchaseRb.isChecked = isChecked.not()
            viewModel.applyFilterOption(if (isChecked && binding.purchaseRb.isChecked.not()) FilterOption.SOLD else FilterOption.NONE)

        }
        binding.ascRb.setOnCheckedChangeListener { _, isChecked ->
            binding.ascRb.isChecked = isChecked
            if (isChecked) binding.descRb.isChecked = isChecked.not()
            viewModel.applySortingOrder(SortingOrder.ASC)
        }
        binding.descRb.setOnCheckedChangeListener { _, isChecked ->
            binding.descRb.isChecked = isChecked
            if (isChecked) binding.ascRb.isChecked = isChecked.not()
            viewModel.applySortingOrder(SortingOrder.DESC)
        }
    }

    private fun setObservers() {
        viewModel.tradesWithCurrentPrice.observe(viewLifecycleOwner) { trades -> setData(trades) }
        viewModel.filteredTrades.observe(viewLifecycleOwner, ::setData)
    }

    private fun setData(trades: List<TradedAssetWithCurrentValue>) {
        tradesAdapter.setTrades(trades)
    }

    private fun onTradeClicked(tradedAssetWithCurrentValue: TradedAssetWithCurrentValue) {
        Toast.makeText(requireContext(), tradedAssetWithCurrentValue.tradeEntity.assetId, Toast.LENGTH_SHORT).show()
    }
}
