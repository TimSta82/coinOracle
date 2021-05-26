package de.bornholdtlee.defaultprojectkotlin.injection

import de.bornholdtlee.defaultprojectkotlin.usecases.RefreshQuestionsFromApiUseCase
import de.bornholdtlee.defaultprojectkotlin.usecases.WatchQuestionsFromDbUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single { RefreshQuestionsFromApiUseCase() }
    single { WatchQuestionsFromDbUseCase() }
}
