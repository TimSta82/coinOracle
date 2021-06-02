package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject

class SellAssetUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()

    suspend fun call(currentAsset: Asset, amount: Double): SellAssetResult {
        val portfolio = portfolioRepository.getPortfolio()
        return if (portfolio.myAssets.isEmpty()) {
            SellAssetResult.NoAssetsAvailableFailure
        } else {
            val assetToSell = portfolio.myAssets.find { it.asset.id == currentAsset.id }
            assetToSell?.let {
                val availableAmount = it.amount
                if (amount > availableAmount) {
                    SellAssetResult.NotEnoughFailure
                } else {
                    val newAmount = assetToSell.amount - amount
                    val newMyAsset = assetToSell.copy(amount = newAmount, timeStamp = System.currentTimeMillis())
                    val updatedMyAssets = portfolio.myAssets.toMutableList()
                    updatedMyAssets.remove(assetToSell)
                    updatedMyAssets.add(newMyAsset)
                    val newBudget = currentAsset.priceEuro.toDouble() * amount
                    val newPortfolio = portfolio.copy(budget = newBudget, myAssets = updatedMyAssets, lastUpdate = System.currentTimeMillis())
                    portfolioRepository.updatePortfolio(newPortfolio)
                    SellAssetResult.Success
                }
            } ?: SellAssetResult.Failure
        }
    }

    sealed class SellAssetResult {
        object Success : SellAssetResult()
        object NoAssetsAvailableFailure : SellAssetResult()
        object NotEnoughFailure : SellAssetResult()
        object Failure : SellAssetResult()
    }
}