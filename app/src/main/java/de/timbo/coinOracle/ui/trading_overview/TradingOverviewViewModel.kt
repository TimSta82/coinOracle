package de.timbo.coinOracle.ui.trading_overview

import androidx.lifecycle.*
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.usecases.WatchTradesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradingOverviewViewModel : ViewModel(), KoinComponent {

    private val watchTradesUseCase by inject<WatchTradesUseCase>()

    val trades: LiveData<List<TradeEntity>> = watchTradesUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _filteredTrades = MutableLiveData<List<TradeEntity>>()
    val filteredTrades: LiveData<List<TradeEntity>> = _filteredTrades

    private val _sortingOrder = MutableLiveData<SortingOrder>()
    val sortingOrder: LiveData<SortingOrder> = _sortingOrder

    fun applyFilterOption(filterOption: FilterOption) {
        _filteredTrades.value = when (filterOption) {
            FilterOption.PURCHASED -> trades.value?.filter { tradeEntity -> tradeEntity.isSold.not() } ?: emptyList()
            FilterOption.SOLD -> trades.value?.filter { tradeEntity -> tradeEntity.isSold } ?: emptyList()
            else -> trades.value
        }
    }

    fun applySortingOrder(sortingOrder: SortingOrder) {
        _sortingOrder.value = sortingOrder
    }

}

enum class FilterOption {
    PURCHASED, SOLD, NONE
}

enum class SortingOrder {
    ASC, DESC, NONE
}
