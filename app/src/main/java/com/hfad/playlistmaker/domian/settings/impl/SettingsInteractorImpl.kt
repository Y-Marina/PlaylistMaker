package com.hfad.playlistmaker.domian.settings.impl

import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.domian.settings.api.SettingsInteractor

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