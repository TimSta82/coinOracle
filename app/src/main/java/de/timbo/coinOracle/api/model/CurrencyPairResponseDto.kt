package de.timbo.coinOracle.api.model


import com.google.gson.annotations.SerializedName

data class CurrencyPairResponseDto(
    @SerializedName("result")
    val result: String?,
    @SerializedName("documentation")
    val documentation: String?,
    @SerializedName("terms_of_use")
    val termsOfUse: String?,
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Int?,
    @SerializedName("time_last_update_utc")
    val timeLastUpdateUtc: String?,
    @SerializedName("time_next_update_unix")
    val timeNextUpdateUnix: Int?,
    @SerializedName("time_next_update_utc")
    val timeNextUpdateUtc: String?,
    @SerializedName("base_code")
    val baseCode: String?,
    @SerializedName("target_code")
    val targetCode: String?,
    @SerializedName("conversion_rate")
    val conversionRate: Double?
)