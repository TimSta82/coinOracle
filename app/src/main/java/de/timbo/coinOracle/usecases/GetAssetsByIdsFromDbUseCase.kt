package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.CorrelatingAssets
import de.timbo.coinOracle.repositories.AssetsRepository
import org.koin.core.component.inject

class GetAssetsByIdsFromDbUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    suspend fun call(correlations: List<CorrelationEntity>): GetAssetsByIdsResult {
        val ids = mutableListOf<String>()
        correlations.let { correlations ->
            correlations.map { correlation ->
                ids.add(correlation.winnerAssetId)
                ids.add(correlation.loserAssetId)
            }
        }
        val assets = assetsRepository.getAssetsByIdsFromDb(ids)
        val correlatingAssetsList = mutableListOf<CorrelatingAssets>()
        correlations.forEach { correlation ->
            val winner = assets.find { assetEntity -> correlation.winnerAssetId == assetEntity.id }
            val loser = assets.find { assetEntity -> correlation.loserAssetId == assetEntity.id }
            if (winner == null) return GetAssetsByIdsResult.WinnerNullFailure
            if (loser == null) return GetAssetsByIdsResult.LoserNullFailure
            correlatingAssetsList.add(CorrelatingAssets(Asset(winner), Asset(loser)))
        }
        return GetAssetsByIdsResult.Success(correlatingAssetsList)
    }

    sealed class GetAssetsByIdsResult {
        data class Success(val correlatingAssets: List<CorrelatingAssets>) : GetAssetsByIdsResult()
        object WinnerNullFailure : GetAssetsByIdsResult()
        object LoserNullFailure : GetAssetsByIdsResult()
    }
}
