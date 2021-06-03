package de.timbo.coinOracle.repositories

import de.timbo.coinOracle.database.dao.CorrelationDao
import de.timbo.coinOracle.database.model.CorrelationEntity
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class CorrelationRepository : BaseRepository() {

    private val dao by inject<CorrelationDao>()

    fun saveAntiCorrelation(correlations: List<CorrelationEntity>) {
        repositoryScope.launch {
            dao.insertOrUpdate(correlations)
        }
    }
}
