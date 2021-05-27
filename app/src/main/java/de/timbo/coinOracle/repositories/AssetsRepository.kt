package de.timbo.coinOracle.repositories

class AssetsRepository : BaseRepository() {

    suspend fun getAssets() = coinApiCall { coinApi.getAssets() }
}