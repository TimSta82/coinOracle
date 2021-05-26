package de.timbo.coracle.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.timbo.coracle.R
import de.timbo.coracle.database.KeyValueStore
import de.timbo.coracle.database.model.QuestionEntity
import de.timbo.coracle.extensions.launch
import de.timbo.coracle.usecases.BaseUseCase.UseCaseResult.Failure
import de.timbo.coracle.usecases.BaseUseCase.UseCaseResult.Success
import de.timbo.coracle.usecases.RefreshQuestionsFromApiUseCase
import de.timbo.coracle.usecases.WatchQuestionsFromDbUseCase
import de.timbo.coracle.utils.Logger
import de.timbo.coracle.utils.SingleLiveEvent
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
