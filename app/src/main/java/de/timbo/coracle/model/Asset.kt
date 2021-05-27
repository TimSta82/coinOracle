package de.timbo.coracle.model

import de.timbo.coracle.api.model.AssetsDto

data class Asset(
    val id: String?,
    val rank: String?,
    val symbol: String?,
    val name: String?,
    val supply: String?,
    val maxSupply: String?,
    val marketCapUsd: String?,
    val volumeUsd24Hr: String?,
    val priceUsd: String?,
    val priceEuro: String?,
    val changePercent24Hr: String?,
    val vwap24Hr: String?,
    val explorer: String?
) {
    constructor(dto: AssetsDto.AssetDto) : this(
        id = dto.id,
        rank = dto.rank,
        symbol = dto.symbol,
        name = dto.name,
        supply = dto.supply,
        maxSupply = dto.maxSupply,
        marketCapUsd = dto.marketCapUsd,
        volumeUsd24Hr = dto.volumeUsd24Hr,
        priceUsd = dto.priceUsd,
        changePercent24Hr = dto.changePercent24Hr,
        vwap24Hr = dto.vwap24Hr,
        explorer = dto.explorer,
        priceEuro = ""
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