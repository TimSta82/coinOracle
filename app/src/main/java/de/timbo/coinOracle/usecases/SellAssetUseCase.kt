package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject

class SellAssetUseCase: BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
}