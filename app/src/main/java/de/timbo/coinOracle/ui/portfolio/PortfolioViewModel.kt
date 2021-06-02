package de.timbo.coinOracle.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.model.PortfolioWithCurrentAssetPrices
import de.timbo.coinOracle.usecases.WatchAssetsFromDbUseCase
import de.timbo.coinOracle.usecases.WatchPortfolioFromDbUseCase
import de.timbo.coinOracle.usecases.WatchPortfolioWithCurrentAssetPricesFromDbUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PortfolioViewModel : ViewModel(), KoinComponent {

    private val watchPortfolioWithCurrentAssetPricesFromDbUseCase by inject<WatchPortfolioWithCurrentAssetPricesFromDbUseCase>()

    val portfolio: LiveData<PortfolioWithCurrentAssetPrices> = watchPortfolioWithCurrentAssetPricesFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)
}
