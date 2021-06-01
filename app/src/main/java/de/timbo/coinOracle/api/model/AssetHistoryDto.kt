package de.timbo.coinOracle.api.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AssetHistoryDto(
    @SerializedName("data")
    val assetHistory: List<AssetHistoryDataDto?>?
) : Parcelable {
    @Parcelize
    data class AssetHistoryDataDto(
        @SerializedName("priceUsd")
        val priceUsd: String?,
        @SerializedName("time")
        val time: Long?
    ) : Parcelable
}