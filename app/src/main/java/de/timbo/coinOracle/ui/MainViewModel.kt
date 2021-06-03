package de.timbo.coinOracle.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.usecases.*
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class MainViewModel : ViewModel(), KoinComponent {

    private val getAssetsUseCase by inject<GetAssetsUseCase>()
    private val getEuroRateUseCase by inject<GetEuroRateUseCase>()
    private val saveAssetsUseCase by inject<SaveAssetsUseCase>()
    private val savePortfolioUseCase by inject<SavePortfolioUseCase>()
    private val calculateAssetsAntiCorrelationUseCase by inject<CalculateAssetsAntiCorrelationUseCase>()
    private val buyAssetUseCase by inject<BuyAssetUseCase>()
    private val sellAssetUseCase by inject<SellAssetUseCase>()

    private val _assetsFailure = SingleLiveEvent<Any>()
    val assetsFailure: LiveData<Any> = _assetsFailure

    private val _euroFailure = SingleLiveEvent<Any>()
    val euroFailure: LiveData<Any> = _euroFailure

    private val _portfolio = MutableLiveData<PortfolioEntity>()
    val portfolio: LiveData<PortfolioEntity> = _portfolio

    private val _portFolioFailure = SingleLiveEvent<Any>()
    val portFolioFailure: LiveData<Any> = _portFolioFailure

    private val _sellSuccess = SingleLiveEvent<Any>()
    val sellSuccess: LiveData<Any> = _sellSuccess

    private val _sellFailure = SingleLiveEvent<String>()
    val sellFailure: LiveData<String> = _sellFailure

    var job: Job? = null

    // TODO uncomment later. Meanwhile save api traffic
    fun startUpdates() {
        stopUpdates()
        job = viewModelScope.launch {
            while (true) {
                getEuroRate()
                delay(30000)
            }
        }
    }

    private fun stopUpdates() {
        job?.cancel()
        job = null
    }

    fun getEuroRate() {
        launch {
            when (val result = getEuroRateUseCase.call()) {
                is BaseUseCase.UseCaseResult.Success -> {
                    val euro = result.resultObject
                    getAssets(euro)
                }
                else -> _euroFailure.callAsync()
            }
        }
    }

    private fun getAssets(euro: CurrencyPairResponseDto) {
        launch {
            when (val result = getAssetsUseCase.call(euro)) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject.let { assets ->
                        val assetsSize = assets.size
                        val randomAsset = assets[Random.nextInt(assetsSize - 1)]

                        sellAsset(randomAsset)
                        buyAsset(randomAsset)
                        calculateCorrelation(assets)
                        saveAssetsUseCase.call(assets)
                    }
                }
                else -> _assetsFailure.callAsync()
            }
        }
    }

    private fun calculateCorrelation(assets: List<Asset>) {
        when (val result = calculateAssetsAntiCorrelationUseCase.call(assets)) {
            CalculateAssetsAntiCorrelationUseCase.CalculateAssetsAntiCorrelationResult.NoCorrelationFailure -> Logger.debug("NoCorrelationFailure")
            CalculateAssetsAntiCorrelationUseCase.CalculateAssetsAntiCorrelationResult.NoLosersFailure -> Logger.debug("NoLosersFailure")
            CalculateAssetsAntiCorrelationUseCase.CalculateAssetsAntiCorrelationResult.NoWinnersFailure -> Logger.debug("NoWinnersFailure")
            CalculateAssetsAntiCorrelationUseCase.CalculateAssetsAntiCorrelationResult.Success -> Logger.debug("Success")
        }
    }

    private suspend fun sellAsset(asset: Asset) {
        when (sellAssetUseCase.call(asset, Random.nextDouble(1.0))) {
            is SellAssetUseCase.SellAssetResult.Success -> {
                Logger.debug("getAssets() called. ${asset.name} sold")
                _sellSuccess.callAsync()
            }
            is SellAssetUseCase.SellAssetResult.NotEnoughFailure -> _sellFailure.postValue("Not enough amount")
            is SellAssetUseCase.SellAssetResult.NoAssetsAvailableFailure -> _sellFailure.postValue("No assets available")
            is SellAssetUseCase.SellAssetResult.Failure -> _sellFailure.postValue("Failure")
        }
    }

    private suspend fun buyAsset(asset: Asset) {
        when (val result = buyAssetUseCase.call(asset, Random.nextDouble(1.0))) {
            is BuyAssetUseCase.BuyAssetResult.Success -> {
                Logger.debug("getAssets() call. ${asset.name} bought")
                _portfolio.postValue(result.portfolio)
            }
            is BuyAssetUseCase.BuyAssetResult.NotEnoughBudget -> {
                Logger.debug("getAssets() call. ${asset.name} NOT bought")
                _portFolioFailure.callAsync()
            }
        }
    }

    fun initPortfolio() {
        launch {
            savePortfolioUseCase.call()
        }
    }
}
