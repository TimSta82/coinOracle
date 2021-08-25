package de.timbo.coinOracle.model

object AssetsMock {

    private val assets = listOf(
        Asset(
            id = "1",
            rank = "1000",
            symbol = "ADA T",
            name = "Cardano T",
            supply = "",
            maxSupply = "",
            marketCapUsd = "",
            volumeUsd24Hr = "",
            priceUsd = "30.00",
            priceEuro = "25.00",
            changePercent24Hr = "25.00",
            vwap24Hr = "",
            explorer = ""
        ),
        Asset(
            id = "2",
            rank = "1000",
            symbol = "BTC T",
            name = "Bitcoin T",
            supply = "",
            maxSupply = "",
            marketCapUsd = "",
            volumeUsd24Hr = "",
            priceUsd = "30.00",
            priceEuro = "25.00",
            changePercent24Hr = "24.00",
            vwap24Hr = "",
            explorer = ""
        ),
        Asset(
            id = "3",
            rank = "1000",
            symbol = "ETH T",
            name = "Ethereum T",
            supply = "",
            maxSupply = "",
            marketCapUsd = "",
            volumeUsd24Hr = "",
            priceUsd = "30.00",
            priceEuro = "25.00",
            changePercent24Hr = "-25.00",
            vwap24Hr = "",
            explorer = ""
        ),
        Asset(
            id = "4",
            rank = "1000",
            symbol = "DOGE T",
            name = "Dogecoin T",
            supply = "",
            maxSupply = "",
            marketCapUsd = "",
            volumeUsd24Hr = "",
            priceUsd = "30.00",
            priceEuro = "25.00",
            changePercent24Hr = "-20.00",
            vwap24Hr = "",
            explorer = ""
        )
    )

    fun getMockAssets() = assets
}