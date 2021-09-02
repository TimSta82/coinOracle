package de.timbo.coinOracle.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.model.MyAsset
import de.timbo.coinOracle.model.PortfolioWithCurrentAssetPrices
import de.timbo.coinOracle.usecases.WatchPortfolioWithCurrentAssetPricesFromDbUseCase
import de.timbo.coinOracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PortfolioViewModel : ViewModel(), KoinComponent {

    private val watchPortfolioWithCurrentAssetPricesFromDbUseCase by inject<WatchPortfolioWithCurrentAssetPricesFromDbUseCase>()

    val portfolio: LiveData<PortfolioWithCurrentAssetPrices> = watchPortfolioWithCurrentAssetPricesFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _filteredList = SingleLiveEvent<List<MyAsset>>()
    val filteredList: LiveData<List<MyAsset>> = _filteredList

    fun search(searchKey: String) {
        val l = portfolio.value?.portfolioEntity?.myAssets?.filter { myAsset -> (myAsset.asset.id.startsWith(searchKey) || (myAsset.asset.symbol.startsWith(searchKey))) } ?: emptyList()
        _filteredList.value = l
    }
}
