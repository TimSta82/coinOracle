package de.timbo.coracle.usecases

import de.timbo.coracle.api.ResponseEvaluator
import de.timbo.coracle.model.Asset
import de.timbo.coracle.repositories.AssetsRepository
import org.koin.core.component.inject

class GetAssetsUseCase : BaseUseCase() {

    private val assetsRepository by inject<AssetsRepository>()
    private val getEuroRateUseCase by inject<GetEuroRateUseCase>()

    suspend fun getAssets(): AssetsResult {
        var rate = -0.1
//        val euro = getEuroRateUseCase.call()
        when (val result = getEuroRateUseCase.call()) {
            is UseCaseResult.Success -> rate = result.resultObject.conversionRate!!
            else -> AssetsResult.CurrencyFailure
        }
        return when (val result = assetsRepository.getAssets()) {
            is ResponseEvaluator.Result.Success -> {
                result.response.body()?.let { assetsDto ->
                    val assets = assetsDto.assets?.map { dto ->
//                        Asset(dto).getPriceEuro(rate)
                        Asset(dto)
                    }
                    assets?.let {
                        AssetsResult.Success(assets)
//                        UseCaseResult.Success(assets)
                    }
//                } ?: UseCaseResult.Failure()
                } ?: AssetsResult.AssetsFailure
            }
            else -> AssetsResult.AssetsFailure
        }
    }

    sealed class AssetsResult {
        data class Success(val assets: List<Asset>): AssetsResult()
        object CurrencyFailure: AssetsResult()
        object AssetsFailure: AssetsResult()
    }
}