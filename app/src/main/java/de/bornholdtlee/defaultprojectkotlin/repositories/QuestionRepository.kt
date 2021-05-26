package de.bornholdtlee.defaultprojectkotlin.repositories

import androidx.lifecycle.LiveData
import de.bornholdtlee.defaultprojectkotlin.api.ResponseEvaluator
import de.bornholdtlee.defaultprojectkotlin.api.model.QuestionListDto
import de.bornholdtlee.defaultprojectkotlin.database.dao.QuestionDao
import de.bornholdtlee.defaultprojectkotlin.database.model.QuestionEntity
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class QuestionRepository : BaseRepository() {

    private val questionDao by inject<QuestionDao>()

    suspend fun startDownload(): ResponseEvaluator.Result<QuestionListDto> = apiCall { api.loadQuestions("android") }

    fun watchAllQuestions(): LiveData<List<QuestionEntity>> = questionDao.watchAll()

    fun saveQuestions(questionListDto: QuestionListDto) {
        repositoryScope.launch {
            questionDao.insert(questionListDto.toQuestionEntityList())
        }
    }
}
