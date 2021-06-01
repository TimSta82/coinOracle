package de.timbo.coinOracle.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyAsset(
    val id: String,
    val name: String,
    val symbol: String,
    val purchasePriceUsd: String,
    val purchasePriceEur: String,
    val amount: Double
) : Parcelable {
    constructor(asset: Asset, amount: Double) : this(
        id = asset.id,
        name = asset.name,
        symbol = asset.symbol,
        purchasePriceUsd = asset.priceUsd,
        purchasePriceEur = asset.priceEuro,
        amount = amount
    )
}
