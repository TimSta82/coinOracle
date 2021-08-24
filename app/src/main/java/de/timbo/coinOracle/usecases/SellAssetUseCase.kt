package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject
import kotlin.random.Random

class SellAssetUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
    private val saveTradeUseCase by inject<SaveTradeUseCase>()

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
                    val newAmount = if (amount == -1.0) 0.0 else assetToSell.amount - amount
                    val newMyAsset = assetToSell.copy(amount = newAmount, timeStamp = System.currentTimeMillis())
                    val updatedMyAssets = portfolio.myAssets.toMutableList()
                    updatedMyAssets.remove(assetToSell)
                    if (newAmount != 0.0000000) updatedMyAssets.add(newMyAsset)
                    val newBudget = if (amount == -1.0) currentAsset.priceEuro.toDouble() * assetToSell.amount else currentAsset.priceEuro.toDouble() * amount
                    val newPortfolio = portfolio.copy(budget = portfolio.budget + newBudget, myAssets = updatedMyAssets, lastUpdate = System.currentTimeMillis())
                    portfolioRepository.updatePortfolio(newPortfolio)
                    saveTradeUseCase.call(
                        TradeEntity(
                            id = Random.nextInt(100000),
                            assetId = currentAsset.id,
                            assetSymbol = currentAsset.symbol,
                            assetValue = currentAsset.priceEuro,
                            amount = amount,
                            timeStamp = System.currentTimeMillis(),
                            isSold = true
                        )
                    )
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