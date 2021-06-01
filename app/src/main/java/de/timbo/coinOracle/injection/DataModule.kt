package de.timbo.coinOracle.injection

import androidx.room.Room
import de.timbo.coinOracle.database.AssetsDb
import de.timbo.coinOracle.database.KeyValueStore
import de.timbo.coinOracle.database.KeyValueStoreEncrypted
import de.timbo.coinOracle.database.PortfolioDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

//    single {
//        Room.databaseBuilder(androidContext(), QuestionDb::class.java, DB_NAME)
//            .fallbackToDestructiveMigration() // TODO: REMOVE DESTRUCTIVE FALLBACK BEFORE SHIPPING TO PROD!!!
//            .addMigrations(MIGRATION_1_2)
//            .build()
//    }

    single {
        Room.databaseBuilder(androidContext(), PortfolioDb::class.java, PortfolioDb.PORTFOLIO_DB_NAME)
            .fallbackToDestructiveMigration() // TODO: REMOVE DESTRUCTIVE FALLBACK BEFORE SHIPPING TO PROD!!!
            .build()
    }
    single { get<PortfolioDb>().portfolioDao() }

    single {
        Room.databaseBuilder(androidContext(), AssetsDb::class.java, AssetsDb.ASSETS_DB_NAME)
            .fallbackToDestructiveMigration() // TODO: REMOVE DESTRUCTIVE FALLBACK BEFORE SHIPPING TO PROD!!!
            .build()
    }
    single { get<AssetsDb>().assetsDao() }

    single { KeyValueStore(androidContext()) }
    single { KeyValueStoreEncrypted(androidContext()) }
}
