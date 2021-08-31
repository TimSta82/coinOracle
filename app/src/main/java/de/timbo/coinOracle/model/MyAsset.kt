package de.timbo.coinOracle.model

import android.os.Parcelable
import de.timbo.coinOracle.extensions.roundOffDecimal
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

    fun getPurchaseValue(currencyType: CurrencyType) = when (currencyType) {
        CurrencyType.DOLLAR -> amount * purchasePriceUsd.toDouble()
        CurrencyType.EURO -> amount * purchasePriceEur.toDouble()
    }

    fun getProfit(currentAsset: Asset, currencyType: CurrencyType) : Double {
        val currentAssetValue = when (currencyType) {
            CurrencyType.DOLLAR -> amount * currentAsset.priceUsd.toDouble()
            CurrencyType.EURO -> amount * currentAsset.priceEuro.toDouble()
        }
        return currentAssetValue - getPurchaseValue(currencyType)
    }
}

enum class CurrencyType {
    EURO, DOLLAR
}
