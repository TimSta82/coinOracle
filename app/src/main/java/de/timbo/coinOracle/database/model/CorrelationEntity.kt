package de.timbo.coinOracle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CorrelationEntity(
    @PrimaryKey
    @ColumnInfo(name = "winner_id")
    val winnerId: String,
    @ColumnInfo(name = "loser_id")
    val loserId: String
)
