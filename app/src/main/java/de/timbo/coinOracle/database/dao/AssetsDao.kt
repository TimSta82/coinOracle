package de.timbo.coinOracle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.AssetEntity

@Dao
abstract class AssetsDao : BaseDao<AssetEntity>() {

    @Query("SELECT * FROM AssetEntity")
    abstract fun watchAll(): LiveData<List<AssetEntity>>

}
