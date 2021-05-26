package de.timbo.coracle.injection

import de.timbo.coracle.usecases.RefreshQuestionsFromApiUseCase
import de.timbo.coracle.usecases.WatchQuestionsFromDbUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single { RefreshQuestionsFromApiUseCase() }
    single { WatchQuestionsFromDbUseCase() }
}
