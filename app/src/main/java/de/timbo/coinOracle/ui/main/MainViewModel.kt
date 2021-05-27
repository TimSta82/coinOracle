package de.timbo.coinOracle.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.timbo.coinOracle.R
import de.timbo.coinOracle.database.KeyValueStore
import de.timbo.coinOracle.database.model.QuestionEntity
import de.timbo.coinOracle.extensions.launch
import de.timbo.coinOracle.usecases.BaseUseCase.UseCaseResult.Failure
import de.timbo.coinOracle.usecases.BaseUseCase.UseCaseResult.Success
import de.timbo.coinOracle.usecases.RefreshQuestionsFromApiUseCase
import de.timbo.coinOracle.usecases.WatchQuestionsFromDbUseCase
import de.timbo.coinOracle.utils.Logger
import de.timbo.coinOracle.utils.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val refreshQuestionsFromApiUseCase by inject<RefreshQuestionsFromApiUseCase>()
    private val watchQuestionsFromDbUseCase by inject<WatchQuestionsFromDbUseCase>()
    private val keyValueStore by inject<KeyValueStore>()

    val questionEntityLiveData: LiveData<List<QuestionEntity>> = watchQuestionsFromDbUseCase.call()

    private val _downloadError: MutableLiveData<Int> = MutableLiveData()
    val downloadError: LiveData<Int> = _downloadError

    private val _downloadSuccess = SingleLiveEvent<Any>()
    val downloadSuccess: LiveData<Any> = _downloadSuccess

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

    fun makeApiCall() {
        launch {
            when (refreshQuestionsFromApiUseCase.call()) {
                is Success -> _downloadSuccess.call()
                is Failure -> _downloadError.postValue(R.string.error_load_questions)
            }
        }
    }
}
