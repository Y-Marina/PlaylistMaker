package com.hfad.playlistmaker.data.settings

interface SettingsRepository {
    fun saveTheme(isNight: Boolean)

    /**
     * Возвращает сохраненную тему
     * Если темная тема - true
     */
    fun getTheme(): Boolean
}