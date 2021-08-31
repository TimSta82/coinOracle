package de.timbo.coinOracle.ui.trading_overview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.databinding.FragmentTradingOverviewBinding
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.viewBinding

class TradingOverviewFragment : BaseFragment(R.layout.fragment_trading_overview) {

    private val viewModel by viewModels<TradingOverviewViewModel>()
    private val binding by viewBinding(FragmentTradingOverviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        // TODO implement switch
        setRadioButtonClicklisteners()
    }

    private fun setRadioButtonClicklisteners() {
        binding.allRb.setOnCheckedChangeListener { _, isChecked ->
            binding.allRb.isChecked = isChecked
            if (isChecked) {
                binding.purchaseRb.isChecked = isChecked.not()
                binding.soldRb.isChecked = isChecked.not()
            }
            viewModel.applyFilter(FilterOption.ALL)
        }
        binding.purchaseRb.setOnCheckedChangeListener { _, isChecked ->
            binding.purchaseRb.isChecked = isChecked
            if (isChecked) {
                binding.allRb.isChecked = isChecked.not()
                binding.soldRb.isChecked = isChecked.not()
            }
            viewModel.applyFilter(FilterOption.PURCHASED)
        }
        binding.soldRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allRb.isChecked = isChecked.not()
                binding.purchaseRb.isChecked = isChecked.not()
            }
            binding.soldRb.isChecked = isChecked
            viewModel.applyFilter(FilterOption.SOLD)
        }
        binding.ascRb.setOnCheckedChangeListener { _, isChecked ->
            binding.ascRb.isChecked = isChecked
            if (isChecked) binding.descRb.isChecked = isChecked.not()
            viewModel.applyFilter(FilterOption.ASC)
        }
        binding.descRb.setOnCheckedChangeListener { _, isChecked ->
            binding.descRb.isChecked = isChecked
            if (isChecked) binding.ascRb.isChecked = isChecked.not()
            viewModel.applyFilter(FilterOption.DESC)
        }
    }

    private fun setObservers() {
        viewModel.trades.observe(viewLifecycleOwner) { trades -> setData(trades) }
        viewModel.filterOptions.observe(viewLifecycleOwner, ::setFilter)
    }

    private fun setFilter(list: List<FilterOption>) {
        Logger.debug("filterList: $list")
    }

    private fun setData(trades: List<TradeEntity>) {
        binding.tradingRv.adapter = TradingOverviewAdapter(trades) { Toast.makeText(requireContext(), it.assetId, Toast.LENGTH_SHORT).show() }
    }
}
