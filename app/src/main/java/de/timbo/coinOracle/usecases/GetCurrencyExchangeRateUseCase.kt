package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.api.ResponseEvaluator
import de.timbo.coinOracle.repositories.CurrencyRepository
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