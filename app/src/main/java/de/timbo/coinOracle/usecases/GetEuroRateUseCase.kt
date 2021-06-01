package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.repositories.CurrencyRepository
import org.koin.core.component.inject

class GetEuroRateUseCase : BaseUseCase(){

    private val currencyRepository by inject<CurrencyRepository>()

    suspend fun call() = responseCall(currencyRepository.getEuroRate())
}
