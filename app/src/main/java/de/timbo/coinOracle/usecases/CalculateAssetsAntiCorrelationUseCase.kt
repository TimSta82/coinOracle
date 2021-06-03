package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.CorrelationRepository
import de.timbo.coinOracle.utils.Logger
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class CalculateAssetsAntiCorrelationUseCase : BaseUseCase() {

    private val correlationRepository by inject<CorrelationRepository>()
    private val buyAssetUseCase by inject<BuyAssetUseCase>()
    private val sellAssetUseCase by inject<SellAssetUseCase>()

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
            considerTrading(winnerAssets[i], loserAssets[i]) // TODO create nice trading logic
            antiCorrelations.add(
                CorrelationEntity(
                    winnerId = winnerAssets[i].id,
                    winnerPercentage24h = winnerAssets[i].changePercent24Hr,
                    loserId = loserAssets[i].id,
                    loserPercentage24h = loserAssets[i].changePercent24Hr
                )
            )
        }
        Logger.debug("cors: $antiCorrelations")
        return antiCorrelations
    }

    private fun considerTrading(winner: Asset, loser: Asset) {
        useCaseScope.launch {
            sellAssetUseCase.call(winner, -1.0)
            buyAssetUseCase.call(loser, -1.0)
        }
    }

    sealed class CalculateAssetsAntiCorrelationResult {
        object Success : CalculateAssetsAntiCorrelationResult()
        object NoWinnersFailure : CalculateAssetsAntiCorrelationResult()
        object NoLosersFailure : CalculateAssetsAntiCorrelationResult()
        object NoCorrelationFailure : CalculateAssetsAntiCorrelationResult()
    }
}
