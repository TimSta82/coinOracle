package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.repositories.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

class WatchPortfolioFromDbUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()

    fun call(): Flow<PortfolioEntity> = portfolioRepository.watchPortfolio()
}
