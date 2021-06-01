package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.timbo.coinOracle.model.MyAsset

@Entity
data class PortfolioEntity(
    @PrimaryKey
    @ColumnInfo(name = "portfolio_id")
    var portfolioId: String,

    @ColumnInfo(name = "last_update")
    var lastUpdate: Long,

    @ColumnInfo(name = "budget")
    var budget: Double,

    @ColumnInfo(name = "my_assets")
    var myAssets: List<MyAsset>
)
