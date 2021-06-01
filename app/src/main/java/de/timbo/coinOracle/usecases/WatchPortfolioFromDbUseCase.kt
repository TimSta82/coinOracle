package de.timbo.coinOracle.usecases

import androidx.lifecycle.LiveData
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject

class WatchPortfolioFromDbUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()

    fun call(): LiveData<PortfolioEntity> = portfolioRepository.watchPortfolio()
}
