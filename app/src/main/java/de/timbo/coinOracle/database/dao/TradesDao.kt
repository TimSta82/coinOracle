package de.timbo.coinOracle.database.dao

import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.TradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TradesDao : BaseDao<TradeEntity>() {

    @Query("SELECT * FROM TradeEntity")
    abstract fun watchAll(): Flow<List<TradeEntity>>

    @Query("DELETE FROM TradeEntity")
    abstract suspend fun deleteAll()
}
