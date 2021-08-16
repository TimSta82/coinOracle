package de.timbo.coinOracle.model

data class TradePreview(
    val newAmount: String,
    val singlePrice: String,
    val totalPrice: Double,
    val totalAmount: Double,
    val oldBudget: Double,
    val newBudget: Double
)