package de.timbo.coinOracle.api

import de.timbo.coinOracle.api.model.AssetHistoryDto
import de.timbo.coinOracle.api.model.AssetsDto
import de.timbo.coinOracle.api.model.QuestionListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinCapApiInterface {

    /**
     * Beispielhafter Aufbau eines Endpoints
     */
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    suspend fun loadQuestions(@Query("tagged") tags: String): Response<QuestionListDto>

    @GET("/v2/assets")
    suspend fun getAssets(): Response<AssetsDto>

    @GET("/v2/assets/{id}/history")
    suspend fun getHistory(@Path ("id") id: String, @Query ("interval") interval: String): Response<AssetHistoryDto>
}
