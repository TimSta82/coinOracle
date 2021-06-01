package de.timbo.coinOracle.model

import android.os.Parcelable
import de.timbo.coinOracle.api.model.AssetsDto

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asset(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val priceEuro: String?,
    val changePercent24Hr: String,
    val vwap24Hr: String,
    val explorer: String
) : Parcelable {
    constructor(dto: AssetsDto.AssetDto) : this(
        id = dto.id ?: "-1",
        rank = dto.rank ?: "-1",
        symbol = dto.symbol ?: "-1",
        name = dto.name ?: "-1",
        supply = dto.supply ?: "-1",
        maxSupply = dto.maxSupply ?: "-1",
        marketCapUsd = dto.marketCapUsd ?: "-1",
        volumeUsd24Hr = dto.volumeUsd24Hr ?: "-1",
        priceUsd = dto.priceUsd ?: "-1",
        changePercent24Hr = dto.changePercent24Hr ?: "-1",
        vwap24Hr = dto.vwap24Hr ?: "-1",
        explorer = dto.explorer ?: "-1",
        priceEuro = null
    )

    fun getPriceEuro(rate: Double): Asset {
        val price = priceUsd?.let {
            it.toDouble() * rate
        } ?: 0.0
        return copy(
            priceEuro = price.toString()
        )
    }
}