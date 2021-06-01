package de.timbo.coinOracle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.PortfolioEntity

@Dao
abstract class PortfolioDao : BaseDao<PortfolioEntity>() {

    @Query("SELECT * FROM PortfolioEntity")
    abstract fun watchPortfolio() : LiveData<PortfolioEntity>
}
