package de.timbo.coracle.injection

import de.timbo.coracle.usecases.*
import org.koin.dsl.module

val useCaseModule = module {

    single { RefreshQuestionsFromApiUseCase() }
    single { WatchQuestionsFromDbUseCase() }

    single { GetAssetsUseCase() }
    single { GetCurrencyExchangeRateUseCase() }
    single { GetEuroRateUseCase() }
}
