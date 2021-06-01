package de.timbo.coinOracle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.timbo.coinOracle.database.dao.PortfolioDao
import de.timbo.coinOracle.database.model.PortfolioEntity

@Database(entities = [PortfolioEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PortfolioDb : RoomDatabase() {

    companion object {
        const val PORTFOLIO_DB_NAME = "portfolioDb"
    }

    abstract fun portfolioDao(): PortfolioDao
}