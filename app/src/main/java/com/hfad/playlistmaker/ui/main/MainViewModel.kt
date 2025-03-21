package com.hfad.playlistmaker.ui.main

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.settings.api.SettingsInteractor

sealed class MainCommand {
    data object NavigationToSearch : MainCommand()
    data object NavigationToMedia : MainCommand()
    data object NavigationToSettings : MainCommand()
}

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