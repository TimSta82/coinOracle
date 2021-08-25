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
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TradeAssetViewModel(private val asset: Asset, private val type: TradingType) : ViewModel(), KoinComponent {

    companion object {
        const val DEFAULT_ZERO = 0.0
    }

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

    private val _previewValues = MutableLiveData<TradePreview?>()
    val previewValues: LiveData<TradePreview?> = _previewValues

    private val _onNotEnoughBudget = SingleLiveEvent<Boolean>()
    val onNotEnoughBudget: LiveData<Boolean> = _onNotEnoughBudget

    private val _onNotEnoughAssetAmount = SingleLiveEvent<Boolean>()
    val onNotEnoughAssetAmount: LiveData<Boolean> = _onNotEnoughAssetAmount

    private val _onSuccess = SingleLiveEvent<Any>()
    val onSuccess: LiveData<Any> = _onSuccess

    private val _onFailure = SingleLiveEvent<Any>()
    val onFailure: LiveData<Any> = _onFailure

    private val _onNavigateUp = SingleLiveEvent<Any>()
    val onNavigateUp: LiveData<Any> = _onNavigateUp

    private val _digits = SingleLiveEvent<Double?>()
    val digits : LiveData<Double?> = _digits

    init {
        _currentAsset.value = asset
        _tradingType.value = type
    }

    fun calculatePreviewValues(amount: Editable?) {
        if (amount.isNullOrEmpty()) {
            _previewValues.value = null
            return
        }
        val a = amount.toString()
            when (type) {
                TradingType.BUY -> calculateBuyPreview(a)
                TradingType.SELL -> calculateSellPreview(a)
                TradingType.CONVERT -> calculateConvertPreview(a)
            }
    }

    private fun calculateConvertPreview(amount: String) {


    }

    private fun calculateSellPreview(amount: String) {
        amount.toDouble().let { amountAsDouble ->
            val priceTotal = amountAsDouble * asset.priceEuro.toDouble()
            val currentAssetAmount = _currentAssetAmount.value ?: DEFAULT_ZERO
            val totalAmount = currentAssetAmount - amountAsDouble
            val budget = portfolio.value?.budget ?: DEFAULT_ZERO
            _onNotEnoughAssetAmount.value = totalAmount < DEFAULT_ZERO
            _previewValues.value = TradePreview(
                newAmount = amountAsDouble.toString(),
                singlePrice = asset.priceEuro,
                totalPrice = priceTotal,
                totalAmount = currentAssetAmount - amountAsDouble,
                oldBudget = budget,
                newBudget = (budget) + priceTotal
            )
        }
    }

    private fun calculateBuyPreview(amount: String) {
        amount.toDouble().let { amountAsDouble ->
            val priceTotal = amountAsDouble * asset.priceEuro.toDouble()
            val currentAssetAmount = _currentAssetAmount.value ?: DEFAULT_ZERO
            val budget = portfolio.value?.budget ?: DEFAULT_ZERO
            _onNotEnoughAssetAmount.value = amountAsDouble > currentAssetAmount
            _onNotEnoughBudget.value = priceTotal > budget
            _previewValues.value = TradePreview(
                newAmount = amountAsDouble.toString(),
                singlePrice = asset.priceEuro,
                totalPrice = priceTotal,
                totalAmount = currentAssetAmount + amountAsDouble,
                oldBudget = budget,
                newBudget = (budget) - priceTotal
            )
        }
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
            }?.amount ?: DEFAULT_ZERO
        } else {
            _currentAssetAmount.value = DEFAULT_ZERO
        }
    }

    fun navigateUp() {
        launch {
            delay(300L)
            _onNavigateUp.callAsync()
        }
    }

    fun getMax() {
        when (type) {
            TradingType.BUY -> prepareMaxPurchase()
            TradingType.SELL -> prepareMaxSellout()
            TradingType.CONVERT -> prepareMaxConversion()
        }

    }
    private fun prepareMaxPurchase() {
        val maxAmountToBuy = (portfolio.value?.budget ?: DEFAULT_ZERO) / asset.priceEuro.toDouble()
        _digits.value = maxAmountToBuy
    }


    private fun prepareMaxSellout() {
        val maxAmountToSell = portfolio.value?.myAssets?.firstOrNull { myAsset -> myAsset.asset.id == asset.id }?.amount ?: DEFAULT_ZERO
        _digits.value = maxAmountToSell
    }

    private fun prepareMaxConversion() {}
}
