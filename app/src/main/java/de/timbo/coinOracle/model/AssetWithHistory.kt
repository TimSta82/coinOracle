package de.timbo.coinOracle.model

import de.timbo.coinOracle.api.model.AssetHistoryDto

data class AssetWithHistory(
    val assetId: String,
    val assetHistoryDto: AssetHistoryDto
)