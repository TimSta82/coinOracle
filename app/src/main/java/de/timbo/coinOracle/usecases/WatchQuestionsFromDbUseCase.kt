package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.QuestionRepository
import org.koin.core.component.inject

class WatchQuestionsFromDbUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

    fun call() = questionRepository.watchAllQuestions()
}
