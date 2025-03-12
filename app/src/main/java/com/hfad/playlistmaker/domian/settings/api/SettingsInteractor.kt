package com.hfad.playlistmaker.domian.settings.api

interface SettingsInteractor {
    fun hasSavedTheme(): Boolean

    fun saveTheme(isNight: Boolean)

    fun getTheme(): Boolean
}