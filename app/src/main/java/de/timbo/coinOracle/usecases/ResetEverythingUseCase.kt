package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.KeyValueStore
import de.timbo.coinOracle.repositories.CorrelationRepository
import de.timbo.coinOracle.repositories.PortfolioRepository
import de.timbo.coinOracle.repositories.TradeRepository
import org.koin.core.component.inject

class ResetEverythingUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
    private val correlationRepository by inject<CorrelationRepository>()
    private val tradeRepository by inject<TradeRepository>()
    private val savePortfolioUseCase by inject<SavePortfolioUseCase>()
    private val keyValueStore by inject<KeyValueStore>()

    suspend fun call() {
        portfolioRepository.deletePortfolio()
        correlationRepository.deleteAll()
        tradeRepository.deleteAllTrades()
        keyValueStore.isPortfolioInitialized = false
        savePortfolioUseCase.call()
    }
}
