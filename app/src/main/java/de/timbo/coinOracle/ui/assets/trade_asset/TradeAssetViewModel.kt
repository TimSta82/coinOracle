package de.timbo.coinOracle.ui.assets.trade_asset

import androidx.lifecycle.*
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.TradePreview
import de.timbo.coinOracle.usecases.WatchPortfolioFromDbUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradeAssetViewModel(private val asset: Asset) : ViewModel(), KoinComponent {


    private val watchPortfolioFromDbUseCase by inject<WatchPortfolioFromDbUseCase>()

    val portfolio: LiveData<PortfolioEntity> = watchPortfolioFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _currentAsset = MutableLiveData<Asset>()
    val currentAsset: LiveData<Asset> = _currentAsset

    private val _currentAssetAmount = MutableLiveData<Double>()
    val currentAssetAmount: LiveData<Double> = _currentAssetAmount

    private val _previewValues = MutableLiveData<TradePreview>()
    val previewValues: LiveData<TradePreview> = _previewValues

    init {
        _currentAsset.value = asset
        _currentAssetAmount.value = portfolio.value?.myAssets?.find { myAsset ->
            myAsset.asset.id == asset.id
        }?.amount ?: 0.0
    }

    fun calculatePreviewValues(amount: String) {
        val priceTotal = amount.toDouble() * asset.priceEuro.toDouble()
        _previewValues.value = TradePreview(
            newAmount = amount,
            singlePrice = asset.priceEuro,
            totalPrice = priceTotal,
            totalAmount = amount.toDouble() + (_currentAssetAmount.value ?: 0.0),
            newBudget = (portfolio.value?.budget ?: 0.0) - priceTotal
        )
    }
}