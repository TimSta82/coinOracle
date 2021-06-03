package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.CorrelationEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.CorrelationRepository
import org.koin.core.component.inject

class CalculateAssetsAntiCorrelationUseCase : BaseUseCase() {

    private val correlationRepository by inject<CorrelationRepository>()

    fun call(assets: List<Asset>): CalculateAssetsAntiCorrelationResult {
        val winnerIds = assets.filter { it.changePercent24Hr.toDouble() >= 5.0 }
        val loserIds = assets.filter { it.changePercent24Hr.toDouble() <= -5.0 }

        return when {
            winnerIds.isNullOrEmpty() -> {
                correlationRepository.saveAntiCorrelation(CorrelationEntity(winnerId = "-1", loserId = loserIds[0].id))
                CalculateAssetsAntiCorrelationResult.NoWinnersFailure
            }
            loserIds.isNullOrEmpty() -> {
                correlationRepository.saveAntiCorrelation(CorrelationEntity(winnerId = winnerIds[0].id, loserId = "-1"))
                CalculateAssetsAntiCorrelationResult.NoLosersFailure
            }
            winnerIds.isNullOrEmpty() && loserIds.isNullOrEmpty() -> CalculateAssetsAntiCorrelationResult.NoCorrelationFailure
            else -> {
                correlationRepository.saveAntiCorrelation(CorrelationEntity(winnerId = winnerIds[0].id, loserId = loserIds[0].id))
                CalculateAssetsAntiCorrelationResult.Success
            }
        }
    }

    sealed class CalculateAssetsAntiCorrelationResult {
        object Success : CalculateAssetsAntiCorrelationResult()
        object NoWinnersFailure : CalculateAssetsAntiCorrelationResult()
        object NoLosersFailure : CalculateAssetsAntiCorrelationResult()
        object NoCorrelationFailure : CalculateAssetsAntiCorrelationResult()
    }
}
