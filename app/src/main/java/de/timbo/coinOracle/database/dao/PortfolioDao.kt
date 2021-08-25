package de.timbo.coinOracle.database.dao

import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.PortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PortfolioDao : BaseDao<PortfolioEntity>() {

    @Query("SELECT * FROM PortfolioEntity")
    abstract fun watchPortfolio(): Flow<PortfolioEntity>

    @Query("SELECT * FROM PortfolioEntity")
    abstract suspend fun getPortfolio(): PortfolioEntity

    @Query("DELETE FROM PortfolioEntity")
    abstract suspend fun deleteAll()
}
