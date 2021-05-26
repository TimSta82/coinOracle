package de.bornholdtlee.defaultprojectkotlin.database

import android.content.Context
import android.content.SharedPreferences
import de.bornholdtlee.defaultprojectkotlin.R
import java.util.*

class KeyValueStore(context: Context) {

    private companion object {
        const val EXAMPLE = "EXAMPLE"
    }

    private val sharedPreferencesReader: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val sharedPreferencesWriter: SharedPreferences.Editor = sharedPreferencesReader.edit()

    var example: Boolean
        get() = getBoolean(EXAMPLE, false)
        set(value) = putBoolean(EXAMPLE, value)

    private fun putString(key: String, value: String?) = sharedPreferencesWriter.putString(key, value).apply()
    private fun putBoolean(key: String, value: Boolean) = sharedPreferencesWriter.putBoolean(key, value).apply()
    private fun putInt(key: String, value: Int) = sharedPreferencesWriter.putInt(key, value).apply()
    private fun putLong(key: String, value: Long) = sharedPreferencesWriter.putLong(key, value).apply()
    private fun putFloat(key: String, value: Float) = sharedPreferencesWriter.putFloat(key, value).apply()

    private fun addToKeySet(key: String, setEntry: String) {
        val set = getKeySet(key).apply { add(setEntry) }
        sharedPreferencesWriter.putStringSet(key, set).apply()
    }

    private fun removeFromKeySet(key: String, setEntry: String) {
        val set = getKeySet(key).apply { remove(setEntry) }
        sharedPreferencesWriter.putStringSet(key, set).apply()
    }

    private fun remove(key: String) = sharedPreferencesWriter.remove(key).apply()
    private fun removeAll() = sharedPreferencesWriter.clear().apply()

    private fun getString(key: String, defaultValue: String): String = sharedPreferencesReader.getString(key, defaultValue) ?: defaultValue
    private fun getBoolean(key: String, defaultValue: Boolean): Boolean = sharedPreferencesReader.getBoolean(key, defaultValue)
    private fun getLong(key: String, defaultValue: Long): Long = sharedPreferencesReader.getLong(key, defaultValue)
    private fun getInt(key: String, defaultValue: Int): Int = sharedPreferencesReader.getInt(key, defaultValue)
    private fun getFloat(key: String, defaultValue: Float): Float = sharedPreferencesReader.getFloat(key, defaultValue)
    private fun getKeySet(key: String): HashSet<String> = HashSet(sharedPreferencesReader.getStringSet(key, HashSet()) ?: hashSetOf())

    private fun contains(key: String): Boolean = sharedPreferencesReader.contains(key)
}
