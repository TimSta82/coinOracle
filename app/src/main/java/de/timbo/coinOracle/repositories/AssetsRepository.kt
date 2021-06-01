package de.timbo.coinOracle.repositories

class AssetsRepository : BaseRepository() {

    suspend fun getAssets() = coinApiCall { coinApi.getAssets() }

    suspend fun getAssetHistory(assetId: String, interval: String) = coinApiCall { coinApi.getHistory(assetId, interval) }
}