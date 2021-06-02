package de.timbo.coinOracle.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyAsset(
    val asset: Asset,
    val amount: Double,
    val timeStamp: Long,
    val purchasePriceUsd: String,
    val purchasePriceEur: String
) : Parcelable {
    constructor(asset: Asset, amount: Double) : this(
        asset = asset,
        amount = amount,
        timeStamp = System.currentTimeMillis(),
        purchasePriceUsd = asset.priceUsd,
        purchasePriceEur = asset.priceEuro
    )
}
