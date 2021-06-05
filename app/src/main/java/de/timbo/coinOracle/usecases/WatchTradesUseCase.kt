package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.TradeRepository
import org.koin.core.component.inject

class WatchTradesUseCase: BaseUseCase() {

    private val tradeRepository by inject<TradeRepository>()

    fun call() = tradeRepository.watchAll()
}
