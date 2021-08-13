package de.timbo.coinOracle.usecases

import de.timbo.coinOracle.api.model.AssetHistoryDto
import de.timbo.coinOracle.database.model.PortfolioEntity
import de.timbo.coinOracle.extensions.convertLongToTime
import de.timbo.coinOracle.model.Asset
import de.timbo.coinOracle.model.AssetMedian
import de.timbo.coinOracle.model.AssetWithHistory
import de.timbo.coinOracle.model.MyAsset
import de.timbo.coinOracle.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.koin.core.component.inject

class ConsiderUseCase : BaseUseCase() {

    private val getAssetHistoryUseCase by inject<GetAssetHistoryUseCase>()

    suspend fun call(portfolio: PortfolioEntity, assets: List<Asset>) {
        /** handle portfolio */
        val ownedAssetIdsForHistoryCheck = portfolio.myAssets.map { myAsset ->
            assets.find { asset ->
                myAsset.asset.id == asset.id
            }
        }.mapNotNull { it?.id }

        when (val result = getAssetsHistory(ownedAssetIdsForHistoryCheck)) {
            is UseCaseResult.Success -> {
                result.resultObject.forEach { assetWithHistory ->
//                    Logger.debug("assetId: ${assetWithHistory.assetId}\n---------------")
//                    assetWithHistory.assetHistoryDto.assetHistory?.forEach { assetHistory ->
//                        assetHistory?.let { history ->
//                            Logger.debug("date: ${history.time?.convertLongToTime() ?: "no date available"} priceUsd: ${history.priceUsd}$")
//                        }
//                    }
//                    Logger.debug("\n----------------")
                    val median = getAssetMedian(assetWithHistory)
                    val currentAsset = assets.find { asset -> asset.id == median.assetId }
                    val lastHistoryEntry = assetWithHistory.assetHistoryDto.assetHistory?.last()
                    val portfolioAsset = portfolio.myAssets.find { myAsset -> myAsset.asset.id == assetWithHistory.assetId }
                    val portfolioRecommendation = getRecommendationForAssetsInPortfolio(portfolioAsset!!, currentAsset!!, median, lastHistoryEntry!!)
                    Logger.debug(
                        "PortfolioAsset -> assetId: ${median.assetId}" +
                            "\nboughtAtValue: ${portfolioAsset!!.purchasePriceUsd}$" +
                            "\ncurrentValue: ${currentAsset!!.priceUsd}$" +
                            "\nmedian: ${median.median}$ " +
                            "\nyesterdayValue: ${lastHistoryEntry?.priceUsd}$" +
                            "\nyesterday: ${lastHistoryEntry?.time?.convertLongToTime() ?: ""}" +
                            "\nshouldSell: ${portfolioRecommendation.name}"
                    )
                }
            }
            else -> Logger.debug("no asset history available")
        }

        /** check market */
        val allOtherAssetIds = assets.mapNotNull { asset ->
            ownedAssetIdsForHistoryCheck.find { id -> asset.id != id }
        }
        when (val result = getAssetsHistory(allOtherAssetIds)) {
            is UseCaseResult.Success -> {
                result.resultObject.forEach { assetWithHistory ->
//                    Logger.debug("assetId: ${assetWithHistory.assetId}\n---------------")
//                    assetWithHistory.assetHistoryDto.assetHistory?.forEach { assetHistory ->
//                        assetHistory?.let { history ->
//                            Logger.debug("date: ${history.time?.convertLongToTime() ?: "no date available"} priceUsd: ${history.priceUsd}$")
//                        }
//                    }
//                    Logger.debug("\n----------------")
                    val median = getAssetMedian(assetWithHistory)
                    val currentAsset = assets.find { asset -> asset.id == median.assetId }
                    val yesterdayEntry = assetWithHistory.assetHistoryDto.assetHistory?.last()
                    val marketRecommendation = getMarketRecommendation(median, currentAsset!!, yesterdayEntry)
                    Logger.debug(
                        "MarketAsset -> assetId: ${median.assetId}" +
                            "\ncurrentValue: ${currentAsset!!.priceUsd}$" +
                            "\nmedian: ${median.median}$ " +
                            "\nyesterdayValue: ${yesterdayEntry?.priceUsd}$" +
                            "\nyesterday: ${yesterdayEntry?.time?.convertLongToTime() ?: ""}" +
                            "\nmarketRecommendation: ${marketRecommendation.name}"
                    )
                }
            }
            else -> Logger.debug("no asset history available")

        }
    }

    private fun getMarketRecommendation(median: AssetMedian, currentAsset: Asset, yesterdayEntry: AssetHistoryDto.AssetHistoryDataDto?): Recommendation {
        val onePercentOfYesterday = yesterdayEntry!!.priceUsd!!.toDouble() / 100
        val currentValueInPercent = currentAsset.priceUsd.toDouble() / onePercentOfYesterday
        val currentDevelopmentInPercent = currentValueInPercent - 100

        return when {
//            currentDevelopmentInPercent >= 15 -> Recommendation.MUST_SELL
//            currentDevelopmentInPercent < 15 && currentDevelopmentInPercent >= 5 -> Recommendation.SELL
//            currentDevelopmentInPercent < 5 && currentDevelopmentInPercent >= -1 -> Recommendation.HOLD
            currentDevelopmentInPercent < -1 && currentDevelopmentInPercent >= -10 -> Recommendation.BUY
            currentDevelopmentInPercent < -10 -> Recommendation.MUST_BUY
            else -> Recommendation.DONT_KNOW
        }
    }

    private fun getRecommendationForAssetsInPortfolio(portfolioAsset: MyAsset, currentAsset: Asset, median: AssetMedian, lastHistoryEntry: AssetHistoryDto.AssetHistoryDataDto): Recommendation {
        val onePercentOfOwnedAsset = portfolioAsset.purchasePriceUsd.toDouble() / 100
        val currentValueInPercent = currentAsset.priceUsd.toDouble() / onePercentOfOwnedAsset
        val currentDevelopmentInPercent = currentValueInPercent - 100

        return when {
            currentDevelopmentInPercent >= 15 -> Recommendation.MUST_SELL
            currentDevelopmentInPercent < 15 && currentDevelopmentInPercent >= 5 -> Recommendation.SELL
            currentDevelopmentInPercent < 5 && currentDevelopmentInPercent >= -1 -> Recommendation.HOLD
            currentDevelopmentInPercent < -1 && currentDevelopmentInPercent >= -10 -> Recommendation.BUY
            currentDevelopmentInPercent < -10 -> Recommendation.MUST_BUY
            else -> Recommendation.DONT_KNOW
        }
    }

    private fun getAssetMedian(assetWithHistory: AssetWithHistory): AssetMedian {
        var median = 0.0
        assetWithHistory.assetHistoryDto.assetHistory?.let { histories ->
            val range = if (histories.isNotEmpty()) histories.size / 3 else 1
            val cumulatedValue = histories.takeLast(range).sumOf { it?.priceUsd?.toDouble() ?: 0.0 }
            median = cumulatedValue / range.toDouble()
            Logger.debug("size: ${histories.size} range: $range")
        }
        return AssetMedian(assetWithHistory.assetId, median)
    }

    private suspend fun getAssetsHistory(idsForHistory: List<String>): UseCaseResult<List<AssetWithHistory>> {
        return withContext(Dispatchers.IO) {
            val assetsWithHistory = mutableListOf<AssetWithHistory>()
            val runningTasks = idsForHistory.map { id ->
                async {
                    val apiResponse = getAssetHistoryUseCase.call(id, "d1")
                    id to apiResponse
                }
            }
            val responses = runningTasks.awaitAll()
            responses.forEach { (id, response) ->
                when (response) {
                    is UseCaseResult.Success -> {
                        assetsWithHistory.add(AssetWithHistory(id, response.resultObject))
                    }
                    else -> Logger.debug("Loading history failed")
                }
            }
            when {
                assetsWithHistory.isNotEmpty() -> UseCaseResult.Success(assetsWithHistory)
                else -> UseCaseResult.Failure()
            }
        }
    }
}

enum class Recommendation {
    SELL, MUST_SELL, HOLD, BUY, MUST_BUY, DONT_KNOW
}