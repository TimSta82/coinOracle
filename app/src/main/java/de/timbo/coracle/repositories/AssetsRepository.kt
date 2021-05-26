package de.timbo.coracle.repositories

class AssetsRepository : BaseRepository() {

    suspend fun getAssets() = apiCall { api.getAssets() }
}