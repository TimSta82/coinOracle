package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.AssetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class WatchAssetsFromDbUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    fun call(): Flow<List<Asset>> {
        return assetsRepository.watchAllAssets().map { value ->
            value.map { assetEntity ->
                Asset(assetEntity)
            }
        }
    }
}
