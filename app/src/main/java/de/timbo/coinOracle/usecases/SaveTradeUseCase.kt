package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.repositories.TradeRepository
import org.koin.core.component.inject

class SaveTradeUseCase : BaseUseCase() {

    private val repository by inject<TradeRepository>()

    fun call(tradeEntity: TradeEntity) = repository.saveTrade(tradeEntity)
}
