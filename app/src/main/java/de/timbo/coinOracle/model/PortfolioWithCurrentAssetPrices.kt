package de.timbo.coinOracle.model

import de.timbo.coinOracle.database.model.PortfolioEntity

data class PortfolioWithCurrentAssetPrices(
    val portfolioEntity: PortfolioEntity,
    val currentAssets: List<Asset>
)