package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.MyAsset
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject

class BuyAssetUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()

    suspend fun call(asset: Asset, budget: Double): BuyAssetResult {
        val portfolio = portfolioRepository.getPortfolio()
        return if (portfolio.budget >= budget) {
            var amount = budget / asset.priceEuro.toDouble()
            val oldAsset = portfolio.myAssets.find { myAsset -> myAsset.id == asset.id }
            if (oldAsset != null) {
                amount += oldAsset.amount
            }
            val newAssets = portfolio.myAssets.toMutableList()
            newAssets.remove(oldAsset)
            val myNewAsset = MyAsset(asset, amount)
            newAssets.add(myNewAsset)

            val price = myNewAsset.amount * myNewAsset.purchasePriceEur.toDouble()
            portfolio.myAssets = newAssets
            portfolio.budget -= price

            portfolioRepository.updatePortfolio(portfolio)
            BuyAssetResult.Success(portfolio)
        } else BuyAssetResult.NotEnoughBudget
    }

    sealed class BuyAssetResult {
        data class Success(val portfolio: PortfolioEntity) : BuyAssetResult()
        object NotEnoughBudget : BuyAssetResult()
    }
}
