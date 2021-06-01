package de.timbo.coinOracle.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import de.timbo.coinOracle.database.model.AssetEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.AssetsRepository
import de.timbo.coinOracle.repositories.QuestionRepository
import org.koin.core.component.inject

class WatchAssetsFromDbUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    fun call(): LiveData<List<Asset>> {
        val entities = assetsRepository.watchAllAssets()
        return Transformations.map(entities) {assetEntities ->
            var tempAssets = mutableListOf<Asset>()
            assetEntities.forEach { entity ->
                tempAssets.add(Asset(entity))
            }
            return@map tempAssets.toList()
        }
    }
}
