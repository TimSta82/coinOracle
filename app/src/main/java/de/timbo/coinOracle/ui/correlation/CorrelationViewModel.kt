package de.timbo.coinOracle.ui.correlation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.usecases.WatchCorrelationsFromDbUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CorrelationViewModel: ViewModel(), KoinComponent {

    private val watchCorrelationsFromDbUseCase by inject<WatchCorrelationsFromDbUseCase>()

    val correlations : LiveData<List<CorrelationEntity>> = watchCorrelationsFromDbUseCase.call().asLiveData(viewModelScope.coroutineContext)
}