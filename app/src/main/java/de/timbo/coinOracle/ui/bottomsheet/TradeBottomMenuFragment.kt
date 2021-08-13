package de.timbo.coinOracle.ui.bottomsheet

import android.os.Bundle
import android.view.View
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentTradeBottomMenuBinding
import de.timbo.coinOracle.utils.viewBinding

class TradeBottomMenuFragment : BaseBottomSheetDialogFragment(R.layout.fragment_trade_bottom_menu) {

    companion object {
        fun createInstance() = TradeBottomMenuFragment()
    }

    private val binding by viewBinding(FragmentTradeBottomMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.tradeBottomMenuBuyBtn.setOnClickListener { onDismiss(requireDialog()) }
    }
}