package de.timbo.coinOracle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.timbo.coinOracle.database.dao.AssetsDao
import de.timbo.coinOracle.database.dao.CorrelationDao
import de.timbo.coinOracle.database.model.AssetEntity
import de.timbo.coinOracle.database.model.CorrelationEntity

@Database(entities = [CorrelationEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CorrelationDb : RoomDatabase() {

    companion object {
        const val CORRELATION_DB_NAME = "correlationDb"
    }

    abstract fun correlationDao(): CorrelationDao
}
