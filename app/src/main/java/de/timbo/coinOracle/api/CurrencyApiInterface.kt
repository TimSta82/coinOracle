package de.timbo.coinOracle.api

import de.timbo.coinOracle.api.model.CurrencyPairResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiInterface {

    companion object {
        const val API_KEY = "640d8c8fc2c98929b0d18d23"
    }

    //    https://v6.exchangerate-api.com/v6/YOUR-API-KEY/pair/EUR/GBP
    @GET("/v6/$API_KEY/pair/USD/{currency}")
    suspend fun getCurrency(@Path("currency") currency: String): Response<Unit>

    @GET("/v6/$API_KEY/pair/USD/EUR")
    suspend fun getEuroRate(): Response<CurrencyPairResponseDto>
}
