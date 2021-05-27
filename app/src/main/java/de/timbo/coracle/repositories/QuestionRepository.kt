package de.timbo.coracle.repositories

import androidx.lifecycle.LiveData
import de.timbo.coracle.api.ResponseEvaluator
import de.timbo.coracle.api.model.QuestionListDto
import de.timbo.coracle.database.dao.QuestionDao
import de.timbo.coracle.database.model.QuestionEntity
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class QuestionRepository : BaseRepository() {

    private val questionDao by inject<QuestionDao>()

    suspend fun startDownload(): ResponseEvaluator.Result<QuestionListDto> = coinApiCall { coinApi.loadQuestions("android") }

    fun watchAllQuestions(): LiveData<List<QuestionEntity>> = questionDao.watchAll()

    fun saveQuestions(questionListDto: QuestionListDto) {
        repositoryScope.launch {
            questionDao.insert(questionListDto.toQuestionEntityList())
        }
    }
}
