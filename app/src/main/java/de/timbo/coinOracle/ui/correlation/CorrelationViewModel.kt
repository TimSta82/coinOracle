package de.timbo.coinOracle.ui.correlation

import androidx.lifecycle.*
import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.model.CorrelatingAssets
import de.timbo.coinOracle.usecases.GetAssetsByIdsFromDbUseCase
import de.timbo.coinOracle.usecases.WatchCorrelationsFromDbUseCase
import de.timbo.coinOracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CorrelationViewModel : ViewModel(), KoinComponent {

    private val getAssetsByIdsFromDbUseCase by inject<GetAssetsByIdsFromDbUseCase>()
    private val watchCorrelationsFromDbUseCase by inject<WatchCorrelationsFromDbUseCase>()

    val correlations: LiveData<List<CorrelationEntity>> = watchCorrelationsFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)

    private val _correlatingAssets = MutableLiveData<List<CorrelatingAssets>>()
    val correlatingAssets: LiveData<List<CorrelatingAssets>> = _correlatingAssets

    private val _failure = SingleLiveEvent<String>()
    val failure: LiveData<String> = _failure

    fun getCorrelatingAssets(correlations: List<CorrelationEntity>?) {
        launch {
            if (correlations == null) {
                _failure.call()
                return@launch
            }
            when (val result = getAssetsByIdsFromDbUseCase.call(correlations)) {
                is GetAssetsByIdsFromDbUseCase.GetAssetsByIdsResult.Success -> _correlatingAssets.postValue(result.correlatingAssets)
                is GetAssetsByIdsFromDbUseCase.GetAssetsByIdsResult.WinnerNullFailure -> _failure.postValue("Winner Null error")
                is GetAssetsByIdsFromDbUseCase.GetAssetsByIdsResult.LoserNullFailure -> _failure.postValue("Loser Null error")
            }
        }
    }
}