package de.timbo.coinOracle.injection

import de.timbo.coinOracle.repositories.*
import org.koin.dsl.module

val repositoryModule = module {

    single { AssetsRepository() }
    single { CurrencyRepository() }
    single { PortfolioRepository() }
    single { CorrelationRepository() }
    single { TradeRepository() }
}
