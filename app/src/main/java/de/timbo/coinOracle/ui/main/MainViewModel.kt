package de.timbo.coinOracle.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.KeyValueStore
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.usecases.BaseUseCase.UseCaseResult.Failure
import de.timbo.coinOracle.usecases.BaseUseCase.UseCaseResult.Success
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val keyValueStore by inject<KeyValueStore>()

    private val _counter: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val counter: LiveData<Int> = _counter

    init {
        readPreferences()
    }

    fun onIncrementCounter() {
        _counter.value = _counter.value?.plus(1)
    }

    private fun readPreferences() {
        val test = keyValueStore.example
        Logger.error("preferences: $test")
    }
}
