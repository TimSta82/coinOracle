package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.model.PortfolioWithCurrentAssetPrices
import de.timbo.coinOracle.repositories.PortfolioRepository
import kotlinx.coroutines.flow.*
import org.koin.core.component.inject

class WatchPortfolioWithCurrentAssetPricesFromDbUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
    private val watchAssetsFromDbUseCase by inject<WatchAssetsFromDbUseCase>()

    fun call(): Flow<PortfolioWithCurrentAssetPrices> {
        val assets = watchAssetsFromDbUseCase.call()
        val portfolio = portfolioRepository.watchPortfolio()

        return portfolio.combine(assets) { p, a ->
            PortfolioWithCurrentAssetPrices(p, a)
        }
    }
}
