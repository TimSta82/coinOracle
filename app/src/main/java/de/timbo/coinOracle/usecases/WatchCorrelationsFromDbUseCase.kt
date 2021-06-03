package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.repositories.CorrelationRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

class WatchCorrelationsFromDbUseCase : BaseUseCase() {

    private val correlationRepository by inject<CorrelationRepository>()

    fun call(): Flow<List<CorrelationEntity>> {
        return correlationRepository.watchAllCorrelations()
    }
}
