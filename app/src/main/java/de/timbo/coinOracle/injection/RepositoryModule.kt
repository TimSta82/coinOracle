package de.timbo.coinOracle.injection

import de.timbo.coinOracle.repositories.AssetsRepository
import de.timbo.coinOracle.repositories.CurrencyRepository
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { AssetsRepository() }
    single { CurrencyRepository() }
    single { PortfolioRepository() }
}
