package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.timbo.coinOracle.model.Asset

@Entity
data class CorrelationEntity(
    @PrimaryKey
    @ColumnInfo(name = "winner_asset")
    val winnerAsset: Asset,
    @ColumnInfo(name = "loser_asset")
    val loserAsset: Asset,
) {
    override fun toString() =
        "Anticorrelation:\n" +
            "wId: ${winnerAsset.id}, wPercentage: ${winnerAsset.changePercent24Hr}\n" +
            "lId: ${loserAsset.id}, lPercentage: ${loserAsset.changePercent24Hr}\n"
}
