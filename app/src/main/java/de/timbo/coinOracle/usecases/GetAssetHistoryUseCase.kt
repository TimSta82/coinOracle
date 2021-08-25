package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.AssetsRepository
import org.koin.core.component.inject

class GetAssetHistoryUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    suspend fun call(assetId: String, interval: String) = responseCall(assetsRepository.getAssetHistory(assetId, interval))
}
