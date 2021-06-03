package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CorrelationEntity(
    @PrimaryKey
    @ColumnInfo(name = "winner_id")
    val winnerId: String,
    @ColumnInfo(name = "winner_percentage_24_h")
    val winnerPercentage24h: String,
    @ColumnInfo(name = "loser_id")
    val loserId: String,
    @ColumnInfo(name = "loser_percentage_24_h")
    val loserPercentage24h: String
) {
    override fun toString() =
        "Anticorrelation:\n" +
            "wId: $winnerId, wPercentage: $winnerPercentage24h\n" +
            "lId: $loserId, lPercentage: $loserPercentage24h\n"

}
