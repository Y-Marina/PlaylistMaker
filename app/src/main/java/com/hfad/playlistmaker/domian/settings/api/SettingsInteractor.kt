package com.hfad.playlistmaker.domian.settings.api

interface SettingsInteractor {
    fun saveTheme(isNight: Boolean)

    fun getTheme(): Boolean
}