package de.timbo.coinOracle.model

data class AssetHistory(
    val priceUsd: String,
    val priceEur: String?,
    val date: String
) {
    override fun toString() = "Price: $priceUsd$ date: $date\n"
}