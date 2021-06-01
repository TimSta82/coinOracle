package de.timbo.coinOracle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.timbo.coinOracle.database.dao.AssetsDao
import de.timbo.coinOracle.database.dao.QuestionDao
import de.timbo.coinOracle.database.model.AssetEntity
import de.timbo.coinOracle.database.model.QuestionEntity

@Database(entities = [AssetEntity::class], version = 1)
abstract class AssetsDb : RoomDatabase() {

    companion object {
        const val ASSETS_DB_NAME = "assetsDb"
    }

    abstract fun assetsDao(): AssetsDao
}
