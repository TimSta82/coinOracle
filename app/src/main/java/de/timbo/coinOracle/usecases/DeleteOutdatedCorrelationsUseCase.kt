package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.CorrelationRepository
import org.koin.core.component.inject

class DeleteOutdatedCorrelationsUseCase : BaseUseCase() {

    private val correlationRepository by inject<CorrelationRepository>()

    suspend fun call() = correlationRepository.deleteAll()
}
