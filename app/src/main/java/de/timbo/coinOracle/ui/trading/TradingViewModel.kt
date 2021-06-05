package de.timbo.coinOracle.ui.trading

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.model.PortfolioWithCurrentAssetPrices
import de.timbo.coinOracle.usecases.WatchAssetsFromDbUseCase
import de.timbo.coinOracle.usecases.WatchPortfolioFromDbUseCase
import de.timbo.coinOracle.usecases.WatchPortfolioWithCurrentAssetPricesFromDbUseCase
import de.timbo.coinOracle.usecases.WatchTradesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradingViewModel : ViewModel(), KoinComponent {

    private val watchTradesUseCase by inject<WatchTradesUseCase>()

    val trades: LiveData<List<TradeEntity>> = watchTradesUseCase.call().asLiveData(viewModelScope.coroutineContext)
}
