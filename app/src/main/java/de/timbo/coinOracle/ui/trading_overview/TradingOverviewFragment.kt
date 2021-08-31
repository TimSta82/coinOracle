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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        // TODO implement switch
    }

    private fun setObservers() {
        viewModel.trades.observe(viewLifecycleOwner) { trades -> setData(trades) }
    }

    private fun setData(trades: List<TradeEntity>) {
        binding.tradingRv.adapter = TradingOverviewAdapter(trades) { Toast.makeText(requireContext(), it.assetId, Toast.LENGTH_SHORT).show() }
    }
}
