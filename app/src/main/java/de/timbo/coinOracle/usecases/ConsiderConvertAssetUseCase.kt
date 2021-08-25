package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.CorrelatingAssets
import de.timbo.coinOracle.model.MyAsset
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class ConsiderConvertAssetUseCase : BaseUseCase() {

    private val sellAssetUseCase by inject<SellAssetUseCase>()
    private val buyAssetUseCase by inject<BuyAssetUseCase>()

    suspend fun call(portfolio: PortfolioEntity, correlatingAssets: List<CorrelatingAssets>) {
        val winnerAssets = mutableListOf<Asset>()
        val loserAssets = mutableListOf<Asset>()
        correlatingAssets.forEach { correlatingAsset ->
            winnerAssets.add(correlatingAsset.winnerAsset)
            loserAssets.add(correlatingAsset.loserAsset)
        }
        val assetsToSell = portfolio.myAssets.filter { myAsset -> myAsset.asset in winnerAssets }
        sellAssets(assetsToSell)
        considerBuyAssets(loserAssets, portfolio)
    }

    private fun considerBuyAssets(loserAssets: MutableList<Asset>, portfolio: PortfolioEntity) {
        val isAssetAlreadyInPortfolio = portfolio.myAssets.any { myAsset -> myAsset.asset.id in loserAssets.map { it.id } }
        if (isAssetAlreadyInPortfolio.not()) {
            val halfBudget = portfolio.budget / 2 // TODO check and tweak budget values
            val budgetForEach = halfBudget / (loserAssets.size + 1)
            loserAssets.forEach { asset ->
                useCaseScope.launch {
                    buyAssetUseCase.call(asset, budgetForEach)
                }
            }
        }
    }

    private suspend fun sellAssets(assetsToSell: List<MyAsset>) {
        assetsToSell.forEach { myAsset ->
            sellAssetUseCase.call(myAsset.asset, myAsset.amount)
        }
    }
}
