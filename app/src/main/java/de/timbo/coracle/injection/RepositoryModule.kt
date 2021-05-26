package de.timbo.coracle.injection

import de.timbo.coracle.repositories.QuestionRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { QuestionRepository() }
}
