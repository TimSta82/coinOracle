package de.timbo.coinOracle.repositories

import androidx.lifecycle.LiveData
import de.timbo.coinOracle.database.dao.AssetsDao
import de.timbo.coinOracle.database.model.AssetEntity
import de.timbo.coinOracle.model.Asset
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class AssetsRepository : BaseRepository() {

    private val assetDao by inject<AssetsDao>()
    suspend fun getAssets() = coinApiCall { coinApi.getAssets() }

    suspend fun getAssetHistory(assetId: String, interval: String) = coinApiCall { coinApi.getHistory(assetId, interval) }

    fun saveAssets(assets: List<Asset>) {
        repositoryScope.launch {
            val assetEntities = assets.map { it.toEntity() }
            assetDao.insertOrUpdate(assetEntities)
        }
    }

    fun watchAllAssets(): LiveData<List<AssetEntity>> = assetDao.watchAll()
}
