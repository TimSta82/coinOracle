package de.timbo.coinOracle.ui.assets

import androidx.lifecycle.*
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

    val assets: LiveData<List<Asset>> = watchAssets.call().asLiveData(viewModelScope.coroutineContext)

    private val _assetHistoryFailure = SingleLiveEvent<Any>()
    val assetHistoryFailure: LiveData<Any> = _assetHistoryFailure

    private val _assetDetails = SingleLiveEvent<AssetDetails>()
    val assetDetails: LiveData<AssetDetails> = _assetDetails

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
