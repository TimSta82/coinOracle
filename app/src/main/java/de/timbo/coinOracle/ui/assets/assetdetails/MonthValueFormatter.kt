package de.timbo.coinOracle.ui.assets.assetdetails

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MonthValueFormatter: ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return when (value) {
            0.0f -> "Jan"
            1.0f -> "Feb"
            2.0f -> "Mar"
            3.0f -> "Apr"
            4.0f -> "May"
            5.0f -> "Jun"
            6.0f -> "Jul"
            7.0f -> "Aug"
            8.0f -> "Sep"
            9.0f -> "Oct"
            10.0f -> "Nov"
            11.0f -> "Dec"
            else -> throw IllegalArgumentException("$value is not a valid month")
        }
    }
}