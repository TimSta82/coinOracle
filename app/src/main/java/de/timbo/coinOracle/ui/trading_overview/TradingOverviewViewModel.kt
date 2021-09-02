package de.timbo.coinOracle.ui.trading_overview

import androidx.lifecycle.*
import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.extensions.second
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.usecases.WatchAssetsFromDbUseCase
import de.timbo.coinOracle.usecases.WatchTradesUseCase
import de.timbo.coinOracle.utils.CombinedLiveData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradingOverviewViewModel : ViewModel(), KoinComponent {

    private val watchTradesUseCase by inject<WatchTradesUseCase>()
    private val watchAssetsFromDbUseCase by inject<WatchAssetsFromDbUseCase>()

    val trades: LiveData<List<TradeEntity>> = watchTradesUseCase.call().asLiveData(viewModelScope.coroutineContext)
    val tradesWithCurrentPrice =
        CombinedLiveData(watchTradesUseCase.call().asLiveData(viewModelScope.coroutineContext), watchAssetsFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)) { liveData ->
            combineStuff(liveData)
        }

    private fun combineStuff(liveData: List<Any?>): List<TradedAssetWithCurrentValue> {
        val trades = if (liveData.first() != null) liveData.first() as List<TradeEntity> else emptyList()
        val assets = if (liveData.second() != null) liveData.second() as List<Asset> else emptyList()
        return trades.map { tradeEntity ->
            val a = assets.filter { asset -> asset.id == tradeEntity.assetId }
            val b = a.first { asset -> asset.id == tradeEntity.assetId }
            TradedAssetWithCurrentValue(tradeEntity, assetCurrentValue = AssetCurrentValue(b.id, b.priceUsd, b.priceEuro))
        }
    }

    private val _filteredTrades = MutableLiveData<List<TradedAssetWithCurrentValue>>()
    val filteredTrades: LiveData<List<TradedAssetWithCurrentValue>> = _filteredTrades

    private val _sortingOrder = MutableLiveData<SortingOrder>()
    val sortingOrder: LiveData<SortingOrder> = _sortingOrder

    fun applyFilterOption(filterOption: FilterOption) {
        _filteredTrades.value = when (filterOption) {
            FilterOption.PURCHASED -> tradesWithCurrentPrice.value?.filter { tradedAssetWithCurrentValue ->
                tradedAssetWithCurrentValue.tradeEntity.isSold.not()
            } ?: emptyList()
            FilterOption.SOLD -> tradesWithCurrentPrice.value?.filter { tradedAssetWithCurrentValue ->
                tradedAssetWithCurrentValue.tradeEntity.isSold
            } ?: emptyList()
            else -> tradesWithCurrentPrice.value
        }
//        _filteredTrades.value = when (filterOption) {
//            FilterOption.PURCHASED -> trades.value?.filter { tradeEntity -> tradeEntity.isSold.not() } ?: emptyList()
//            FilterOption.SOLD -> trades.value?.filter { tradeEntity -> tradeEntity.isSold } ?: emptyList()
//            else -> trades.value
//        }
    }

    fun applySortingOrder(sortingOrder: SortingOrder) {
        _sortingOrder.value = sortingOrder
    }

}

data class TradedAssetWithCurrentValue(
    val tradeEntity: TradeEntity,
    val assetCurrentValue: AssetCurrentValue
)

data class AssetCurrentValue(
    val assetId: String,
    val priceUsd: String,
    val priceEuro: String
)

enum class FilterOption {
    PURCHASED, SOLD, NONE
}

enum class SortingOrder {
    ASC, DESC, NONE
}
