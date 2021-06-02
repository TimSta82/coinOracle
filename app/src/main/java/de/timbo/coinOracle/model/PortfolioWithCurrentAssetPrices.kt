package de.timbo.coinOracle.model

import de.timbo.coinOracle.database.model.PortfolioEntity

data class PortfolioWithCurrentAssetPrices(
    val portfolioEntity: PortfolioEntity,
    val currentAssets: List<Asset>
) {
    fun getCumulatedValueOfOwnedAssets(): Double {
        var cumulatedValue = 0.000
        portfolioEntity.myAssets.forEach { myAsset ->
            val currentAsset = currentAssets.find { myAsset.asset.id == it.id }
            currentAsset?.let { asset ->
                cumulatedValue += myAsset.amount * asset.priceEuro.toDouble()
            }
        }
        return cumulatedValue
    }
}