package de.bornholdtlee.defaultprojectkotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.bornholdtlee.defaultprojectkotlin.R
import de.bornholdtlee.defaultprojectkotlin.database.KeyValueStore
import de.bornholdtlee.defaultprojectkotlin.database.model.QuestionEntity
import de.bornholdtlee.defaultprojectkotlin.extensions.launch
import de.bornholdtlee.defaultprojectkotlin.usecases.BaseUseCase.UseCaseResult.Failure
import de.bornholdtlee.defaultprojectkotlin.usecases.BaseUseCase.UseCaseResult.Success
import de.bornholdtlee.defaultprojectkotlin.usecases.RefreshQuestionsFromApiUseCase
import de.bornholdtlee.defaultprojectkotlin.usecases.WatchQuestionsFromDbUseCase
import de.bornholdtlee.defaultprojectkotlin.utils.Logger
import de.bornholdtlee.defaultprojectkotlin.utils.SingleLiveEvent
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
