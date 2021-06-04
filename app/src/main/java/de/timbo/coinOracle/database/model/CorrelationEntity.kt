package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CorrelationEntity(
    @PrimaryKey
    @ColumnInfo(name = "winner_asset_id")
    var winnerAssetId: String,
    @ColumnInfo(name = "loser_asset_id")
    var loserAssetId: String
) {
    override fun toString() =
        "Anticorrelation:\n" +
            "wId: $winnerAssetId" +
            "lId: $loserAssetId\n"
}
