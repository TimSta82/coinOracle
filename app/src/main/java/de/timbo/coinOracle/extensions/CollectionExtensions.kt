package de.timbo.coinOracle.extensions

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.util.*
import kotlin.NoSuchElementException

fun <T> List<T>.second(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[1]
}

fun <T> List<T>.replace(index: Int, new: T): List<T> = this.mapIndexed { i, item -> if (i == index) new else item }

fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

fun Long.convertLongToTime(): String {
    val date = java.sql.Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return format.format(date)
}