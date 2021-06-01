package de.timbo.coinOracle.ui.portfolio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.databinding.FragmentPortfolioBinding
import de.timbo.coinOracle.extensions.showSnackBar
import de.timbo.coinOracle.ui.BaseFragment
import de.timbo.coinOracle.utils.viewBinding

class PortfolioFragment : BaseFragment(R.layout.fragment_portfolio) {

    private val binding by viewBinding(FragmentPortfolioBinding::bind)
    private val viewModel by viewModels<PortfolioViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
    }

    private fun setObservers() {
        viewModel.portfolio.observe(viewLifecycleOwner, ::setData)
    }

    private fun setData(portfolioEntity: PortfolioEntity) {
        binding.portfolioAssetsRv.adapter = PortfolioAdapter(portfolioEntity.myAssets) { asset -> showSnackBar(asset.name) }
    }
}