package com.hfad.playlistmaker.ui.main

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.creator.Creator

sealed class MainCommand {
    data object NavigationToSearch: MainCommand()
    data object NavigationToMedia: MainCommand()
    data object NavigationToSettings: MainCommand()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(application) }

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