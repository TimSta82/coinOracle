package de.timbo.coinOracle.repositories

import de.timbo.coinOracle.database.dao.TradesDao
import de.timbo.coinOracle.database.model.TradeEntity
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class TradeRepository : BaseRepository() {

    private val dao by inject<TradesDao>()

    fun saveTrade(tradeEntity: TradeEntity) {
        repositoryScope.launch {
            dao.insert(tradeEntity)
        }
    }

    fun watchAll() = dao.watchAll()
}
