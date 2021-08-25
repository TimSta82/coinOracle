package de.timbo.coinOracle.ui.trading_overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.usecases.WatchTradesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradingOverviewViewModel : ViewModel(), KoinComponent {

    private val watchTradesUseCase by inject<WatchTradesUseCase>()

    val trades: LiveData<List<TradeEntity>> = watchTradesUseCase.call().asLiveData(viewModelScope.coroutineContext)
}
