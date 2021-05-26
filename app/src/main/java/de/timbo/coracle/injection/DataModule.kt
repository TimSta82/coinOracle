package de.timbo.coracle.injection

import androidx.room.Room
import de.timbo.coracle.database.KeyValueStore
import de.timbo.coracle.database.KeyValueStoreEncrypted
import de.timbo.coracle.database.MIGRATION_1_2
import de.timbo.coracle.database.QuestionDb
import de.timbo.coracle.database.QuestionDb.Companion.DB_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), QuestionDb::class.java, DB_NAME)
            .fallbackToDestructiveMigration() // TODO: REMOVE DESTRUCTIVE FALLBACK BEFORE SHIPPING TO PROD!!!
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<QuestionDb>().questionDao() }

    single { KeyValueStore(androidContext()) }
    single { KeyValueStoreEncrypted(androidContext()) }
}
