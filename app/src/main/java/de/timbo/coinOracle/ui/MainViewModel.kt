package de.timbo.coinOracle.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.usecases.*
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val getAssetsUseCase by inject<GetAssetsUseCase>()
    private val getEuroRateUseCase by inject<GetEuroRateUseCase>()
    private val saveAssetsUseCase by inject<SaveAssetsUseCase>()
    private val savePortfolioUseCase by inject<SavePortfolioUseCase>()
    private val buyAssetUseCase by inject<BuyAssetUseCase>()

    private val _assetsFailure = SingleLiveEvent<Any>()
    val assetsFailure: LiveData<Any> = _assetsFailure

    private val _euroFailure = SingleLiveEvent<Any>()
    val euroFailure: LiveData<Any> = _euroFailure

    private val _portfolio = MutableLiveData<PortfolioEntity>()
    val portfolio: LiveData<PortfolioEntity> = _portfolio

    private val _portFolioFailure = SingleLiveEvent<Any>()
    val portFolioFailure: LiveData<Any> = _portFolioFailure

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
            when (val result = getAssetsUseCase.getAssets(euro)) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject.let { assets ->
                        val cardano = assets.find { it.symbol == "ADA" }
                        cardano?.let {
                            Logger.debug("getAssets() call. Cardano found")
                            when (val result = buyAssetUseCase.call(it, 10.0)) {
                                is BuyAssetUseCase.BuyAssetResult.Success -> {
                                    Logger.debug("getAssets() call. Cardano bought")
                                    _portfolio.postValue(result.portfolio)
                                }
                                is BuyAssetUseCase.BuyAssetResult.NotEnoughBudget -> {
                                    Logger.debug("getAssets() call. Cardano NOT bought")
                                    _portFolioFailure.callAsync()
                                }
                            }
                        }
                        saveAssetsUseCase.call(assets)
                    }
                }
                else -> _assetsFailure.callAsync()
            }
        }
    }

    fun initPortfolio() {
        launch {
            savePortfolioUseCase.call()
        }
    }
}
