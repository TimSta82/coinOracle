package de.timbo.coinOracle.database.dao

import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.CorrelationEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CorrelationDao : BaseDao<CorrelationEntity>() {

    @Query("SELECT * FROM CorrelationEntity")
    abstract fun watchAll(): Flow<List<CorrelationEntity>>

    @Query("DELETE FROM CorrelationEntity")
    abstract suspend fun deleteAll()
}
