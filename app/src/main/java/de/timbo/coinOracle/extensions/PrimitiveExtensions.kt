package de.timbo.coinOracle.extensions

import android.content.res.Resources
import java.math.RoundingMode
import java.text.DecimalFormat

fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBoolean() = this == 1

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Double.roundOffDecimal(): String {
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this)
}