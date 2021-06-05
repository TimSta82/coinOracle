package de.timbo.coinOracle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.timbo.coinOracle.database.dao.AssetsDao
import de.timbo.coinOracle.database.dao.TradesDao
import de.timbo.coinOracle.database.model.AssetEntity
import de.timbo.coinOracle.database.model.TradeEntity

@Database(entities = [TradeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TradesDb : RoomDatabase() {

    companion object {
        const val TRADES_DB_NAME = "tradesDb"
    }

    abstract fun tradesDao(): TradesDao
}
