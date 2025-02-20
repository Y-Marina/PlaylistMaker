package com.hfad.playlistmaker.data.settings

interface SettingsRepository {
    fun hasSavedTheme(): Boolean
    fun saveTheme(isNight: Boolean)

    /**
     * Возвращает сохраненную тему
     * Если темная тема - true
     */
    fun getTheme(): Boolean
}