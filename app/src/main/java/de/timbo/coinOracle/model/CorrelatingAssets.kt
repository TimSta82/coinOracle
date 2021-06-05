package de.timbo.coinOracle.model

data class CorrelatingAssets(
    val winnerAsset: Asset,
    val loserAsset: Asset
)