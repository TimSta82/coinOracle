package de.timbo.coinOracle.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.repositories.AssetsRepository
import de.timbo.coinOracle.repositories.PortfolioRepository
import org.koin.core.component.inject

class WatchPortfolioWithCurrentAssetPricesFromDbUseCase : BaseUseCase() {

    private val portfolioRepository by inject<PortfolioRepository>()
    private val assetRepository by inject<AssetsRepository>()

    fun call(): LiveData<PortfolioEntity> {
        val assetsEntities = assetRepository.watchAllAssets()
        val portfolio = portfolioRepository.watchPortfolio()

        return Transformations.map(portfolio) { portfolioEntity ->
            val tempPortfolio: PortfolioEntity
            val myAssetsWithCurrentPrices = portfolioEntity.myAssets
            myAssetsWithCurrentPrices.map { myAsset ->
                val assets = assetsEntities.value?.map { Asset(it) }
                val asset = assets?.find { it.id == myAsset.id }
                return@map myAsset.copy(purchasePriceEur = asset?.priceEuro ?: "-1", purchasePriceUsd = asset?.priceUsd ?: "-1")
            }

//            val myAssetsWithCurrentPrices = portfolioEntity.myAssets.map { myAsset ->
//                val assetEntities = assets.value?.filter { myAsset.id == it.id }
//                return@map assetEntities!!.forEach { assetEntity ->
//                    MyAsset(Asset(assetEntity), amount = myAsset.amount)
//                }
//            }
            tempPortfolio = portfolioEntity.copy(myAssets = myAssetsWithCurrentPrices)
            return@map tempPortfolio
        }
    }
}
