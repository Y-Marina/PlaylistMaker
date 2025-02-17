package com.hfad.playlistmaker.domian.impl

import android.content.SharedPreferences
import com.hfad.playlistmaker.data.settings.SettingsRepository

class SettingsRepositoryImpl(val sharedPreferences: SharedPreferences) : SettingsRepository {

    companion object{
        const val THEME_KEY = "key_for_theme"
    }

    override fun hasSavedTheme(): Boolean {
        return !sharedPreferences.contains(THEME_KEY)
    }

    override fun saveTheme(isNight: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, isNight).apply()
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }
}