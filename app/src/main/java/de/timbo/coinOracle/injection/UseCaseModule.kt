package de.timbo.coinOracle.injection

import de.timbo.coinOracle.usecases.*
import org.koin.dsl.module

val useCaseModule = module {

    single { GetAssetsUseCase() }
    single { GetCurrencyExchangeRateUseCase() }
    single { GetEuroRateUseCase() }
    single { GetAssetHistoryUseCase() }
    single { SaveAssetsUseCase() }
    single { WatchAssetsFromDbUseCase() }
    single { GetAssetsByIdsFromDbUseCase() }

    single { ConsiderTradingUseCase() }
    single { BuyAssetUseCase() }
    single { SellAssetUseCase() }
    single { SavePortfolioUseCase() }
    single { GetPortfolioUseCase() }
    single { WatchPortfolioFromDbUseCase() }
    single { WatchPortfolioWithCurrentAssetPricesFromDbUseCase() }

    single { CalculateAssetsAntiCorrelationUseCase() }
    single { WatchCorrelationsFromDbUseCase() }
    single { DeleteOutdatedCorrelationsUseCase() }
}
