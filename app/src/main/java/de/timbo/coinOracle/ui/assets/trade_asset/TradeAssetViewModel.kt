package de.timbo.coinOracle.ui.assets.trade_asset

import android.text.Editable
import androidx.lifecycle.*
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.TradePreview
import de.timbo.coinOracle.model.TradingType
import de.timbo.coinOracle.usecases.BuyAssetUseCase
import de.timbo.coinOracle.usecases.SellAssetUseCase
import de.timbo.coinOracle.usecases.WatchPortfolioFromDbUseCase
import de.timbo.coinOracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradeAssetViewModel(private val asset: Asset, private val type: TradingType) : ViewModel(), KoinComponent {

    private val watchPortfolioFromDbUseCase by inject<WatchPortfolioFromDbUseCase>()
    private val buyAssetUseCase by inject<BuyAssetUseCase>()
    private val sellAssetUseCase by inject<SellAssetUseCase>()

    val portfolio: LiveData<PortfolioEntity> = watchPortfolioFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _currentAsset = MutableLiveData<Asset>()
    val currentAsset: LiveData<Asset> = _currentAsset

    private val _tradingType = MutableLiveData<TradingType>()
    val tradingType: LiveData<TradingType> = _tradingType

    private val _currentAssetAmount = MutableLiveData<Double>()
    val currentAssetAmount: LiveData<Double> = _currentAssetAmount

    private val _previewValues = MutableLiveData<TradePreview>()
    val previewValues: LiveData<TradePreview> = _previewValues

    private val _onNotEnoughBudget = SingleLiveEvent<Boolean>()
    val onNotEnoughBudget: LiveData<Boolean> = _onNotEnoughBudget

    private val _onNotEnoughAssetAmount = SingleLiveEvent<Boolean>()
    val onNotEnoughAssetAmount: LiveData<Boolean> = _onNotEnoughAssetAmount

    private val _onSuccess = SingleLiveEvent<Any>()
    val onSuccess: LiveData<Any> = _onSuccess

    private val _onFailure = SingleLiveEvent<Any>()
    val onFailure: LiveData<Any> = _onFailure

    init {
        _currentAsset.value = asset
        _tradingType.value = type
    }

    fun calculatePreviewValues(amount: Editable?) {
        if (amount.isNullOrEmpty()) return
        when (type) {
            TradingType.BUY -> calculateBuyPreview(amount)
            TradingType.SELL -> calculateSellPreview(amount)
            TradingType.CONVERT -> calculateConvertPreview(amount)
        }
    }

    private fun calculateConvertPreview(amount: Editable) {


    }

    private fun calculateSellPreview(amount: Editable) {
        val amountAsDouble = amount.toString().toDouble()
        val priceTotal = (amountAsDouble) * asset.priceEuro.toDouble()
        val currentAssetAmount = _currentAssetAmount.value ?: 0.0
        val totalAmount = currentAssetAmount - amountAsDouble
        val budget = portfolio.value?.budget ?: 0.0
        _onNotEnoughAssetAmount.value = totalAmount < 0.0
        _previewValues.value = TradePreview(
            newAmount = amount.toString(),
            singlePrice = asset.priceEuro,
            totalPrice = priceTotal,
            totalAmount = currentAssetAmount - amountAsDouble,
            oldBudget = budget,
            newBudget = (budget) + priceTotal
        )
    }

    private fun calculateBuyPreview(amount: Editable) {
        val amountAsDouble = amount.toString().toDouble()
        val priceTotal = (amountAsDouble) * asset.priceEuro.toDouble()
        val currentAssetAmount = _currentAssetAmount.value ?: 0.0
        val budget = portfolio.value?.budget ?: 0.0
        _onNotEnoughAssetAmount.value = amountAsDouble > currentAssetAmount
        _onNotEnoughBudget.value = priceTotal > budget
        _previewValues.value = TradePreview(
            newAmount = amount.toString(),
            singlePrice = asset.priceEuro,
            totalPrice = priceTotal,
            totalAmount = currentAssetAmount + amountAsDouble,
            oldBudget = budget,
            newBudget = (budget) - priceTotal
        )
    }

    fun confirmTrade(tradingType: TradingType) {
        when (tradingType) {
            TradingType.BUY -> buyAsset()
            TradingType.SELL -> sellAsset()
            TradingType.CONVERT -> convertAsset()
        }

    }

    private fun buyAsset() {
        launch {
            _previewValues.value?.let {
                when (buyAssetUseCase.call(asset, it.totalPrice)) {
                    is BuyAssetUseCase.BuyAssetResult.Success -> _onSuccess.callAsync()
                    else -> _onFailure.callAsync()
                }
            }
        }
    }

    private fun sellAsset() {
        launch {
            _previewValues.value?.let {
                when (sellAssetUseCase.call(asset, it.newAmount.toDouble())) {
                    is SellAssetUseCase.SellAssetResult.Success -> _onSuccess.callAsync()
                    is SellAssetUseCase.SellAssetResult.NoAssetsAvailableFailure -> _onFailure.callAsync()
                    is SellAssetUseCase.SellAssetResult.NotEnoughFailure -> _onFailure.callAsync()
                    else -> _onFailure.callAsync()
                }
            }
        }
    }

    private fun convertAsset() {

    }

    fun initAmount(portfolioEntity: PortfolioEntity) {
        if (portfolioEntity.myAssets.isNotEmpty()) {
            _currentAssetAmount.value = portfolioEntity.myAssets.firstOrNull { myAsset ->
                myAsset.asset.id == asset.id
            }?.amount ?: 0.0
        } else {
            _currentAssetAmount.value = 0.0
        }
    }
}
