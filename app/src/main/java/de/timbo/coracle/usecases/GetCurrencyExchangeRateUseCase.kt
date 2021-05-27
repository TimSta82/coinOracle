package de.timbo.coracle.usecases

import de.timbo.coracle.api.ResponseEvaluator
import de.timbo.coracle.repositories.CurrencyRepository
import org.koin.core.component.inject

class GetCurrencyExchangeRateUseCase: BaseUseCase() {

    private val currencyRepository by inject<CurrencyRepository>()

    suspend fun call(currency: String) : UseCaseResult<Unit>{
        return when (val result = currencyRepository.getCurrencyExchangeRate(currency)) {
            is ResponseEvaluator.Result.Success -> {
                result.response.body()?.let {
                    UseCaseResult.Success(it)
                } ?: UseCaseResult.Failure()
            }
            else -> UseCaseResult.Failure()
        }
    }
}