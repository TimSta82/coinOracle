package de.timbo.coinOracle.repositories

import androidx.lifecycle.LiveData
import de.timbo.coinOracle.database.dao.PortfolioDao
import de.timbo.coinOracle.database.model.PortfolioEntity
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class PortfolioRepository : BaseRepository() {

    private val portfolioDao by inject<PortfolioDao>()

    fun watchPortfolio(): LiveData<PortfolioEntity> = portfolioDao.watchPortfolio()

    suspend fun getPortfolio(): PortfolioEntity = portfolioDao.getPortfolio()

    fun updatePortfolio(portfolioEntity: PortfolioEntity) {
        repositoryScope.launch {
            portfolioDao.insertOrUpdate(portfolioEntity)
        }
    }
}
