package com.hfad.playlistmaker.ui.main

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.settings.api.SettingsInteractor

sealed class MainCommand {
    data object NavigationToSearch: MainCommand()
    data object NavigationToMedia: MainCommand()
    data object NavigationToSettings: MainCommand()
}

class MainViewModel(
    application: Application,
    settingsInteractor: SettingsInteractor
) : AndroidViewModel(application) {
    init {
        if (settingsInteractor.hasSavedTheme()) {
            val theme = settingsInteractor.getTheme()
            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else {
            val isNightMode = (application.resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            settingsInteractor.saveTheme(isNightMode)
        }
    }

    private val commandLiveData = SingleLiveEvent<MainCommand>()
    fun observeCommand(): LiveData<MainCommand> = commandLiveData

    fun onSearchClick() {
        commandLiveData.postValue(MainCommand.NavigationToSearch)
    }

    fun onMediaClick() {
        commandLiveData.postValue(MainCommand.NavigationToMedia)
    }

    fun onSettingsClick() {
        commandLiveData.postValue(MainCommand.NavigationToSettings)
    }
}