package de.timbo.coinOracle.model

import android.os.Parcelable
import de.timbo.coinOracle.api.model.AssetsDto
import de.timbo.coinOracle.database.model.AssetEntity

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
    val priceEuro: String,
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
        priceEuro = "-1"
    )

    constructor(entity: AssetEntity) : this(
        id = entity.id,
        rank = entity.rank,
        symbol = entity.symbol,
        name = entity.name,
        supply = entity.supply,
        maxSupply = entity.maxSupply,
        marketCapUsd = entity.marketCapUsd,
        volumeUsd24Hr = entity.volumeUsd24Hr,
        priceUsd = entity.priceUsd,
        changePercent24Hr = entity.changePercent24Hr,
        vwap24Hr = entity.vwap24Hr,
        explorer = entity.explorer,
        priceEuro = entity.priceEur
    )

    fun toEntity() = AssetEntity(
        id = id,
        rank = rank,
        symbol = symbol,
        name = name,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd,
        volumeUsd24Hr = volumeUsd24Hr,
        priceUsd = priceUsd,
        priceEur = priceEuro ?: "-1",
        changePercent24Hr = changePercent24Hr,
        vwap24Hr = vwap24Hr,
        explorer = explorer
    )

    fun getPriceEuro(rate: Double): Asset {
        val price = priceUsd.toDouble() * rate
        return copy(
            priceEuro = price.toString()
        )
    }
}