package de.timbo.coinOracle.injection

import de.timbo.coinOracle.usecases.*
import org.koin.dsl.module

val useCaseModule = module {

    single { RefreshQuestionsFromApiUseCase() }
    single { WatchQuestionsFromDbUseCase() }

    single { GetAssetsUseCase() }
    single { GetCurrencyExchangeRateUseCase() }
    single { GetEuroRateUseCase() }
    single { GetAssetHistoryUseCase() }
    single { SaveAssetsUseCase() }
    single { WatchAssetsFromDbUseCase() }
}
