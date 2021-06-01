package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.KeyValueStore
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject

class SavePortfolioUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
    private val keyValueStore by inject<KeyValueStore>()

    suspend fun call() {
        val portfolio: PortfolioEntity
        if (keyValueStore.isPortfolioInitialized) {
            portfolio = portfolioRepository.getPortfolio()
        } else {
            val now = System.currentTimeMillis()
            portfolio = PortfolioEntity("1", now, 10.0, emptyList())
            keyValueStore.isPortfolioInitialized = true
        }
        portfolioRepository.updatePortfolio(portfolio)
    }
}
