package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.TradeEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.MyAsset
import de.timbo.coinOracle.repositories.PortfolioRepository
import de.timbo.coinOracle.utils.Logger
import org.koin.core.component.inject
import kotlin.random.Random

class BuyAssetUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
    private val saveTradeUseCase by inject<SaveTradeUseCase>()

    suspend fun call(asset: Asset, budget: Double): BuyAssetResult {
        val portfolio = portfolioRepository.getPortfolio()
        return if (portfolio.budget >= budget && portfolio.budget > 0) {
            var amount = budget / asset.priceEuro.toDouble()
            Logger.debug("Bought ${asset.name} for $budget€ and get $amount of shares at price ${asset.priceEuro}")

            val oldAsset = portfolio.myAssets.find { myAsset -> myAsset.asset.id == asset.id }
            if (oldAsset != null) {
                amount += oldAsset.amount
            }
            val newAssets = portfolio.myAssets.toMutableList()
            newAssets.remove(oldAsset)
            val myNewAsset = MyAsset(asset, amount)
            newAssets.add(myNewAsset)

            portfolio.myAssets = newAssets
            portfolio.budget -= budget
            portfolioRepository.updatePortfolio(portfolio)
            saveTradeUseCase.call(
                TradeEntity(
                    id = Random.nextInt(100000),
                    assetId = asset.id,
                    assetSymbol = asset.symbol,
                    assetValue = asset.priceEuro,
                    amount = amount,
                    timeStamp = System.currentTimeMillis(),
                    isSold = false,
                )
            )
            BuyAssetResult.Success
        } else BuyAssetResult.NotEnoughBudget
    }

    sealed class BuyAssetResult {
        object Success : BuyAssetResult()
        object NotEnoughBudget : BuyAssetResult()
    }
}
