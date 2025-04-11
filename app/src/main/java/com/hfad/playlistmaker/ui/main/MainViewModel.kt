package com.hfad.playlistmaker.ui.main

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.hfad.playlistmaker.domian.settings.api.SettingsInteractor

class MainViewModel(
    settingsInteractor: SettingsInteractor
) : ViewModel() {
    init {
        val isNightMode = settingsInteractor.getTheme()
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}