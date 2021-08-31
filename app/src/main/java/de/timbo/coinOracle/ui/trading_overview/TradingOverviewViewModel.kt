package de.timbo.coinOracle.ui.trading_overview

import androidx.lifecycle.*
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.usecases.WatchTradesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradingOverviewViewModel : ViewModel(), KoinComponent {

    private val watchTradesUseCase by inject<WatchTradesUseCase>()

    val trades: LiveData<List<TradeEntity>> = watchTradesUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _filterOptions = MutableLiveData<List<FilterOption>>()
    val filterOptions : LiveData<List<FilterOption>> = _filterOptions

    var filterList = mutableListOf<FilterOption>(FilterOption.ALL, FilterOption.ASC)

    fun applyFilter(filterOption: FilterOption) {
        if (filterList.contains(filterOption)) filterList.remove(filterOption) else filterList.add(filterOption)
        _filterOptions.value = filterList
    }
}

enum class FilterOption {
    ALL, PURCHASED, SOLD, ASC, DESC
}
