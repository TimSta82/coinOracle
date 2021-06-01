package de.timbo.coinOracle.ui.assets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.AssetDetails
import de.timbo.coinOracle.usecases.*
import de.timbo.coinOracle.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssetsViewModel : ViewModel(), KoinComponent {

    private val watchAssets by inject<WatchAssetsFromDbUseCase>()
    private val getAssetHistoryUseCase by inject<GetAssetHistoryUseCase>()

    val assets: LiveData<List<Asset>> = watchAssets.call()

    private val _assetHistoryFailure = SingleLiveEvent<Any>()
    val assetHistoryFailure: LiveData<Any> = _assetHistoryFailure

    private val _assetDetails = SingleLiveEvent<AssetDetails>()
    val assetDetails: LiveData<AssetDetails> = _assetDetails

    var job: Job? = null

    // TODO uncomment later. Meanwhile save api traffic
//    fun startUpdates() {
//        stopUpdates()
//        job = viewModelScope.launch {
//            while (true) {
//                getEuroRate()
//                delay(30000)
//            }
//        }
//    }
//
//    private fun stopUpdates() {
//        job?.cancel()
//        job = null
//    }

//    fun getEuroRate() {
//        launch {
//            when (val result = getEuroRateUseCase.call()) {
//                is BaseUseCase.UseCaseResult.Success -> {
//                    val euro = result.resultObject
//                    getAssets(euro)
//                }
//                else -> _euroFailure.callAsync()
//            }
//        }
//    }

//    private fun getAssets(euro: CurrencyPairResponseDto) {
//        launch {
//            when (val result = getAssetsUseCase.getAssets(euro)) {
//                is BaseUseCase.UseCaseResult.Success -> {
//                    result.resultObject.let { assets ->
//                        _assets.postValue(assets)
//                    }
//                }
//                else -> _assetsFailure.callAsync()
//            }
//        }
//    }

    fun getAssetHistory(asset: Asset) {
        launch {
            when (val result = getAssetHistoryUseCase.call(asset.id!!, "d1")) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject.let { assetHistory ->
                        val assetDetails = AssetDetails(asset, assetHistory)
                        _assetDetails.postValue(assetDetails)
                    }
                }
                else -> _assetHistoryFailure.callAsync()
            }
        }
    }
}
