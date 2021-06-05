package de.timbo.coinOracle.ui.trading

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.databinding.FragmentTradingBinding
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

class TradingFragment : BaseFragment(R.layout.fragment_trading) {

    private val viewModel by viewModels<TradingViewModel>()
    private val binding by viewBinding(FragmentTradingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
    }

    private fun setObservers() {
        viewModel.trades.observe(viewLifecycleOwner) { trades -> setData(trades) }
    }

    private fun setData(trades: List<TradeEntity>) {
        binding.tradingRv.adapter = TradingAdapter(trades) { Toast.makeText(requireContext(), it.assetId, Toast.LENGTH_SHORT).show() }
    }
}
