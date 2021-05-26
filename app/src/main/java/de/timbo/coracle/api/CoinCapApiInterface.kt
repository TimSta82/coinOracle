package de.timbo.coracle.api

import de.timbo.coracle.api.model.AssetsDto
import de.timbo.coracle.api.model.QuestionListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinCapApiInterface {

    /**
     * Beispielhafter Aufbau eines Endpoints
     */
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    suspend fun loadQuestions(@Query("tagged") tags: String): Response<QuestionListDto>

    @GET("/v2/assets")
    suspend fun getAssets(): Response<AssetsDto>
}
