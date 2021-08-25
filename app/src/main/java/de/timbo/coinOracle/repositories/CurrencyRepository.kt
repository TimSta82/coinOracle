package de.timbo.coinOracle.repositories

class CurrencyRepository : BaseRepository() {

    suspend fun getCurrencyExchangeRate(currency: String) = currencyApiCall { currencyApi.getCurrency(currency) }

    suspend fun getEuroRate() = currencyApiCall { currencyApi.getEuroRate() }
}
