package de.timbo.coracle.api

import de.timbo.coracle.api.model.AssetsDto
import de.timbo.coracle.api.model.QuestionListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {

    companion object {
        const val API_KEY = "640d8c8fc2c98929b0d18d23"
    }
    @GET("/v6/$API_KEY/latest/USD")
    suspend fun getCurrency(): Response<Unit>
}
