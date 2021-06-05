package de.timbo.coinOracle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.timbo.coinOracle.database.dao.AssetsDao
import de.timbo.coinOracle.database.model.AssetEntity

@Database(entities = [AssetEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AssetsDb : RoomDatabase() {

    companion object {
        const val ASSETS_DB_NAME = "assetsDb"
    }

    abstract fun assetsDao(): AssetsDao
}
