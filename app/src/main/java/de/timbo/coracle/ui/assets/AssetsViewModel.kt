package de.timbo.coracle.ui.assets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.timbo.coracle.extensions.launch
import de.timbo.coracle.model.Asset
import de.timbo.coracle.usecases.BaseUseCase
import de.timbo.coracle.usecases.GetAssetsUseCase
import de.timbo.coracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssetsViewModel : ViewModel(), KoinComponent {

    private val getAssetsUseCase by inject<GetAssetsUseCase>()

    private val _assets = MutableLiveData<List<Asset>>()
    val assets: LiveData<List<Asset>> = _assets

    private val _assetsFailure = SingleLiveEvent<Any>()
    val assetsFailure: LiveData<Any> = _assetsFailure

    fun getAssets() {
        launch {
            when (val result = getAssetsUseCase.getAssets()) {
                is BaseUseCase.UseCaseResult.Success -> {
                    result.resultObject?.let { assets ->
                        _assets.postValue(assets)
                    }
                }
                is BaseUseCase.UseCaseResult.Failure -> _assetsFailure.callAsync()
            }
        }
    }
}