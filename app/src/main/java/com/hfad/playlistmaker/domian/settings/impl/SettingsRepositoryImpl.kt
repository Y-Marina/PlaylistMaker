package com.hfad.playlistmaker.domian.settings.impl

import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.data.storage.LocalStorage

class SettingsRepositoryImpl(private val localStorage: LocalStorage) : SettingsRepository {

    companion object{
        const val THEME_KEY = "key_for_theme"
    }

    override fun hasSavedTheme(): Boolean {
        return localStorage.hasSavedTheme()
    }

    override fun saveTheme(isNight: Boolean) {
        localStorage.saveTheme(isNight)
    }

    override fun getTheme(): Boolean {
        return localStorage.getTheme()
    }
}