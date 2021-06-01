package de.timbo.coinOracle.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.usecases.WatchPortfolioWithCurrentAssetPricesFromDbUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PortfolioViewModel : ViewModel(), KoinComponent {

    //    private val watchPortfolioUseCase by inject<WatchPortfolioFromDbUseCase>()
    private val watchPortfolioWithCurrentAssetPricesFromDbUseCase by inject<WatchPortfolioWithCurrentAssetPricesFromDbUseCase>()

    val portfolio: LiveData<PortfolioEntity> = watchPortfolioWithCurrentAssetPricesFromDbUseCase.call()
}