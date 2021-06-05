package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AssetEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "rank")
    var rank: String,
    @ColumnInfo(name = "symbol")
    var symbol: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "supply")
    var supply: String,
    @ColumnInfo(name = "max_supply")
    var maxSupply: String,
    @ColumnInfo(name = "market_cap_usd")
    var marketCapUsd: String,
    @ColumnInfo(name = "volume_usd_24_hr")
    var volumeUsd24Hr: String,
    @ColumnInfo(name = "price_usd")
    var priceUsd: String,
    @ColumnInfo(name = "price_eur")
    var priceEur: String,
    @ColumnInfo(name = "change_percent_24_hr")
    var changePercent24Hr: String,
    @ColumnInfo(name = "vwap_24_hr")
    var vwap24Hr: String,
    @ColumnInfo(name = "explorer")
    var explorer: String
)
