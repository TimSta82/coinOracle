package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.api.ResponseEvaluator
import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.AssetsRepository
import org.koin.core.component.inject

class GetAssetsUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    suspend fun call(euro: CurrencyPairResponseDto): UseCaseResult<List<Asset>> {
        return when (val result = assetsRepository.getAssets()) {
            is ResponseEvaluator.Result.Success -> {
                result.response.body()?.let { assetsDto ->
                    val assets = assetsDto.assets?.map { dto ->
                        euro.conversionRate?.let {
                            Asset(dto).getPriceEuro(it)
                        } ?: Asset(dto).getPriceEuro(-1.0)
                    }
                    assets?.let {
                        UseCaseResult.Success(assets)
                    }
                } ?: UseCaseResult.Failure()
            }
            else -> UseCaseResult.Failure()
        }
    }
}
