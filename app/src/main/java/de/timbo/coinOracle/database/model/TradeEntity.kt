package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.timbo.coinOracle.model.Asset

@Entity
data class TradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "asset_id")
    val assetId: String,
    @ColumnInfo(name = "asset_symbol")
    val assetSymbol: String,
    @ColumnInfo(name = "asset_value")
    val assetValue: String,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long,
    @ColumnInfo(name = "is_sold")
    val isSold: Boolean
)
