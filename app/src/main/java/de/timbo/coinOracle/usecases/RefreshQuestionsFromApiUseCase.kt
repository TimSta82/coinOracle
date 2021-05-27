package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.QuestionRepository
import org.koin.core.component.inject

class RefreshQuestionsFromApiUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

    suspend fun call() = simpleResponseCall(questionRepository.startDownload()) { questionListDto -> questionRepository.saveQuestions(questionListDto) }
}
