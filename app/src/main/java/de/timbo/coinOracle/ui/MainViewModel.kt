package de.timbo.coinOracle.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.CorrelatingAssets
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
    private val calculateAssetsAntiCorrelationUseCase by inject<CalculateAssetsAntiCorrelationUseCase>()
    private val watchPortfolioFromDbUseCase by inject<WatchPortfolioFromDbUseCase>()

    private val getAssetsByIdsFromDbUseCase by inject<GetAssetsByIdsFromDbUseCase>()
    private val watchCorrelationsFromDbUseCase by inject<WatchCorrelationsFromDbUseCase>()
    private val getPortfolioUseCase by inject<GetPortfolioUseCase>()
    private val considerTradingUseCase by inject<ConsiderTradingUseCase>()

    private val _assetsFailure = SingleLiveEvent<Any>()
    val assetsFailure: LiveData<Any> = _assetsFailure

    private val _euroFailureMessage = SingleLiveEvent<String>()
    val euroFailureMessage: LiveData<String> = _euroFailureMessage

    val portfolio: LiveData<PortfolioEntity> = watchPortfolioFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)
    val correlations: LiveData<List<CorrelationEntity>> = watchCorrelationsFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _correlatingAssetsFailure = SingleLiveEvent<String>()
    val correlatingAssetsFailure: LiveData<String> = _correlatingAssetsFailure

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
                delay(60000)
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
                is GetEuroRateUseCase.GetEuroRateResult.Success -> {
                    val euro = result.result
                    getAssets(euro)
                }
                is GetEuroRateUseCase.GetEuroRateResult.RateLimitReached -> {
                    getAssets(CurrencyPairResponseDto.getDefaultCurrency())
                }
                else -> _euroFailureMessage.callAsync()
            }
        }
    }

    private fun getAssets(euro: CurrencyPairResponseDto) {
        launch {
            when (val result = getAssetsUseCase.call(euro)) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject.let { assets ->
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
            CalculateAssetsAntiCorrelationUseCase.CalculateAssetsAntiCorrelationResult.Success -> Logger.debug("Correlation created successfully")
        }
    }

    fun initPortfolio() {
        launch {
            savePortfolioUseCase.call()
        }
    }

    fun getCorrelatingAssets(correlations: List<CorrelationEntity>?) {
        launch {
            if (correlations == null) {
                _correlatingAssetsFailure.call()
                return@launch
            }
            when (val result = getAssetsByIdsFromDbUseCase.call(correlations)) {
                is GetAssetsByIdsFromDbUseCase.GetAssetsByIdsResult.Success -> {
                    considerTrading(result.correlatingAssets)
                }
                is GetAssetsByIdsFromDbUseCase.GetAssetsByIdsResult.WinnerNullFailure -> _correlatingAssetsFailure.postValue("Winner Null error")
                is GetAssetsByIdsFromDbUseCase.GetAssetsByIdsResult.LoserNullFailure -> _correlatingAssetsFailure.postValue("Loser Null error")
            }
        }
    }

    private fun considerTrading(correlatingAssets: List<CorrelatingAssets>) {
        if (correlatingAssets.isEmpty()) return
        launch {
            val portfolio = getPortfolioUseCase.call()
            considerTradingUseCase.call(portfolio, correlatingAssets)
        }
    }
}
