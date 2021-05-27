package de.timbo.coinOracle.ui.assets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.usecases.BaseUseCase
import de.timbo.coinOracle.usecases.GetAssetsUseCase
import de.timbo.coinOracle.usecases.GetEuroRateUseCase
import de.timbo.coinOracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssetsViewModel : ViewModel(), KoinComponent {

    private val getAssetsUseCase by inject<GetAssetsUseCase>()
    private val getEuroRateUseCase by inject<GetEuroRateUseCase>()

    private val _assets = MutableLiveData<List<Asset>>()
    val assets: LiveData<List<Asset>> = _assets

    private val _assetsFailure = SingleLiveEvent<Any>()
    val assetsFailure: LiveData<Any> = _assetsFailure

    private val _euroFailure = SingleLiveEvent<Any>()
    val euroFailure: LiveData<Any> = _euroFailure

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
                        _assets.postValue(assets)
                    }
                }
                else -> _assetsFailure.callAsync()
            }
        }
    }
}