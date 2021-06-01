package de.timbo.coinOracle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.timbo.coinOracle.database.dao.QuestionDao
import de.timbo.coinOracle.database.model.QuestionEntity

@Database(entities = [QuestionEntity::class], version = 1)
abstract class QuestionDb : RoomDatabase() {

    companion object {
        const val DB_NAME = "questionDb"
    }

    abstract fun questionDao(): QuestionDao
}
