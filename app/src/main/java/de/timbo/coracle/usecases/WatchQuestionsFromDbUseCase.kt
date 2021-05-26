package de.timbo.coracle.usecases

import de.timbo.coracle.repositories.QuestionRepository
import org.koin.core.component.inject

class WatchQuestionsFromDbUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

    fun call() = questionRepository.watchAllQuestions()
}
