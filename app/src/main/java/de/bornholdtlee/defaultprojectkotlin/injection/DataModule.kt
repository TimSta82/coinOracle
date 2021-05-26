package de.bornholdtlee.defaultprojectkotlin.injection

import androidx.room.Room
import de.bornholdtlee.defaultprojectkotlin.database.KeyValueStore
import de.bornholdtlee.defaultprojectkotlin.database.KeyValueStoreEncrypted
import de.bornholdtlee.defaultprojectkotlin.database.MIGRATION_1_2
import de.bornholdtlee.defaultprojectkotlin.database.QuestionDb
import de.bornholdtlee.defaultprojectkotlin.database.QuestionDb.Companion.DB_NAME
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
