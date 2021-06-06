package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import de.timbo.coinOracle.repositories.CurrencyRepository
import org.koin.core.component.inject

class GetEuroRateUseCase : BaseUseCase() {

    private val currencyRepository by inject<CurrencyRepository>()

    suspend fun call(): GetEuroRateResult {
        return when (val result = responseCall(currencyRepository.getEuroRate())) {
            is UseCaseResult.Success -> {
                if (result.resultObject.result == "error") {
                    GetEuroRateResult.RateLimitReached
                } else {
                    GetEuroRateResult.Success(result.resultObject)
                }
            }
            else -> GetEuroRateResult.Failure
        }
//        return responseCall(currencyRepository.getEuroRate())
    }

    sealed class GetEuroRateResult {
        data class Success(val result: CurrencyPairResponseDto) : GetEuroRateResult()
        object RateLimitReached : GetEuroRateResult()
        object Failure : GetEuroRateResult()
    }
}
