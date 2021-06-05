package de.timbo.coinOracle.ui.portfolio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import de.timbo.coinOracle.R
import de.timbo.coinOracle.databinding.FragmentPortfolioBinding
import de.timbo.coinOracle.extensions.showSnackBar
import de.timbo.coinOracle.model.PortfolioWithCurrentAssetPrices
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

    private fun setData(portfolioWithCurrentAssetPrices: PortfolioWithCurrentAssetPrices) {
        binding.portfolioBudgetTv.text = "Available budget: ${portfolioWithCurrentAssetPrices.portfolioEntity.budget}€"
        binding.portfolioCumulatedValueTv.text = "Cumulated value: ${portfolioWithCurrentAssetPrices.getCumulatedValueOfOwnedAssets()}€"
        binding.portfolioAssetsRv.adapter = PortfolioAdapter(portfolioWithCurrentAssetPrices) { myAsset -> showSnackBar(myAsset.asset.name) }
    }
}
