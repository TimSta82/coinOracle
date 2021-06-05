package de.timbo.coinOracle.database.dao

import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AssetsDao : BaseDao<AssetEntity>() {

    @Query("SELECT * FROM AssetEntity")
    abstract fun watchAll(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM AssetEntity WHERE id IN (:ids)")
    abstract suspend fun getAssetsByIds(ids: List<String>): List<AssetEntity>

}
