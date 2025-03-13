package com.hfad.playlistmaker.domian.settings.impl

import android.content.Context
import android.content.res.Configuration
import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.data.storage.LocalStorage

class SettingsRepositoryImpl(
    private val localStorage: LocalStorage,
    private val context: Context
    ) : SettingsRepository {

    companion object{
        const val THEME_KEY = "key_for_theme"
    }

    override fun saveTheme(isNight: Boolean) {
        localStorage.saveTheme(isNight)
    }

    override fun getTheme(): Boolean {
        if (localStorage.hasSavedTheme()) {
            return localStorage.getTheme()
        } else {
            val isNightMode = (context.resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            localStorage.saveTheme(isNightMode)
            return isNightMode
        }
    }
}