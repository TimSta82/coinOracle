package de.timbo.coracle.injection

import de.timbo.coracle.repositories.AssetsRepository
import de.timbo.coracle.repositories.CurrencyRepository
import de.timbo.coracle.repositories.QuestionRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { QuestionRepository() }
    single { AssetsRepository() }
    single { CurrencyRepository() }
}
