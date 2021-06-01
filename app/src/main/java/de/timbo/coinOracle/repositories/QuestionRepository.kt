package de.timbo.coinOracle.repositories

import androidx.lifecycle.LiveData
import de.timbo.coinOracle.api.ResponseEvaluator
import de.timbo.coinOracle.api.model.QuestionListDto
import de.timbo.coinOracle.database.dao.QuestionDao
import de.timbo.coinOracle.database.model.QuestionEntity
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
