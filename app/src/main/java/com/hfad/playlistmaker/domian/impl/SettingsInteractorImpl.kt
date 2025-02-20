package com.hfad.playlistmaker.domian.impl

import android.content.SharedPreferences
import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.domian.api.SettingsInteractor

class SettingsInteractorImpl(val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun hasSavedTheme(): Boolean {
       return settingsRepository.hasSavedTheme()
    }

    override fun saveTheme(isNight: Boolean) {
        settingsRepository.saveTheme(isNight)
    }

    override fun getTheme(): Boolean {
        return settingsRepository.getTheme()
    }
}