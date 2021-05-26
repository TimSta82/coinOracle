package de.bornholdtlee.defaultprojectkotlin.usecases

import de.bornholdtlee.defaultprojectkotlin.repositories.QuestionRepository
import org.koin.core.component.inject

class WatchQuestionsFromDbUseCase : BaseUseCase() {

    private val questionRepository by inject<QuestionRepository>()

    fun call() = questionRepository.watchAllQuestions()
}
