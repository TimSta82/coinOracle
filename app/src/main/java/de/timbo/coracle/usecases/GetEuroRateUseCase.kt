package de.timbo.coracle.usecases

import de.timbo.coracle.repositories.CurrencyRepository
import org.koin.core.component.inject

class GetEuroRateUseCase : BaseUseCase(){

    private val currencyRepository by inject<CurrencyRepository>()

    suspend fun call() = responseCall(currencyRepository.getEuroRate())
}
