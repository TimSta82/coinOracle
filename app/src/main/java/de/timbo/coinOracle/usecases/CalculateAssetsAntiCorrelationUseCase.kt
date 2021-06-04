package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.CorrelationRepository
import de.timbo.coinOracle.utils.Logger
import org.koin.core.component.inject

class CalculateAssetsAntiCorrelationUseCase : BaseUseCase() {

    private val correlationRepository by inject<CorrelationRepository>()

    fun call(assets: List<Asset>): CalculateAssetsAntiCorrelationResult {
        val winnerAssets = assets.filter { asset -> asset.changePercent24Hr.toDouble() >= 10.0 }.sortedWith(compareBy { it.changePercent24Hr }).reversed()
        val loserAssets = assets.filter { asset -> asset.changePercent24Hr.toDouble() <= -0.1 }.sortedWith(compareBy { it.changePercent24Hr }).reversed()

        val antiCorrelations = getAntiCorrelations(winnerAssets, loserAssets)
        return when {
            antiCorrelations.isNullOrEmpty() -> when {
                winnerAssets.isNullOrEmpty() -> {
//                    correlationRepository.saveAntiCorrelation()
                    CalculateAssetsAntiCorrelationResult.NoWinnersFailure
                }
                loserAssets.isNullOrEmpty() -> {
//                    correlationRepository.saveAntiCorrelation()
                    CalculateAssetsAntiCorrelationResult.NoLosersFailure
                }
                else -> CalculateAssetsAntiCorrelationResult.NoCorrelationFailure
            }
            else -> {
                correlationRepository.saveAntiCorrelation(antiCorrelations)
                CalculateAssetsAntiCorrelationResult.Success
            }
        }
    }

    private fun getAntiCorrelations(winnerAssets: List<Asset>, loserAssets: List<Asset>): MutableList<CorrelationEntity> {
        val wSize = winnerAssets.size
        val lSize = loserAssets.size
        val size = if (wSize >= lSize) lSize else wSize

        val antiCorrelations = mutableListOf<CorrelationEntity>()
        for (i in 0 until size) {
            Logger.debug("cors: size: $size")
            antiCorrelations.add(
                CorrelationEntity(
                    winnerAssetId = winnerAssets[i].id,
                    loserAssetId = loserAssets[i].id
                )
            )
        }
        Logger.debug("cors: $antiCorrelations")
        return antiCorrelations
    }

    sealed class CalculateAssetsAntiCorrelationResult {
        object Success : CalculateAssetsAntiCorrelationResult()
        object NoWinnersFailure : CalculateAssetsAntiCorrelationResult()
        object NoLosersFailure : CalculateAssetsAntiCorrelationResult()
        object NoCorrelationFailure : CalculateAssetsAntiCorrelationResult()
    }
}
