package de.timbo.coracle.repositories

class AssetsRepository : BaseRepository() {

    suspend fun getAssets() = coinApiCall { coinApi.getAssets() }
}