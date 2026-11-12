package com.Zudxofi5.Toltoq.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int = 0): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readFlag(key: String, fallback: Boolean = false): Boolean = prefs.getBoolean(key, fallback)

    fun writeFlag(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun wipe() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val STORE_NAME = "joker_theatre_heist"
    }
}
