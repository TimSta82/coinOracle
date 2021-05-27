package de.timbo.coracle.usecases

import de.timbo.coracle.api.ResponseEvaluator
import de.timbo.coracle.api.model.CurrencyPairResponseDto
import de.timbo.coracle.model.Asset
import de.timbo.coracle.repositories.AssetsRepository
import org.koin.core.component.inject

class GetAssetsUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()

    suspend fun getAssets(euro: CurrencyPairResponseDto): UseCaseResult<List<Asset>> {
        return when (val result = assetsRepository.getAssets()) {
            is ResponseEvaluator.Result.Success -> {
                result.response.body()?.let { assetsDto ->
                    val assets = assetsDto.assets?.map { dto ->
                        Asset(dto).getPriceEuro(euro.conversionRate!!)
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