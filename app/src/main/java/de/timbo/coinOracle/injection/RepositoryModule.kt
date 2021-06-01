package de.timbo.coinOracle.injection

import de.timbo.coinOracle.repositories.AssetsRepository
import de.timbo.coinOracle.repositories.CurrencyRepository
import de.timbo.coinOracle.repositories.QuestionRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { QuestionRepository() }
    single { AssetsRepository() }
    single { CurrencyRepository() }
}
