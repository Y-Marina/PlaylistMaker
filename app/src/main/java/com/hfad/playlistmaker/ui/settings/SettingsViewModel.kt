package com.hfad.playlistmaker.ui.settings

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.creator.Creator

data class SettingsUiState(
    val isNightMode: Boolean
)
sealed class SettingsCommand {
    data object NavigateToShare : SettingsCommand()
    data object NavigateToSupport : SettingsCommand()
    data object NavigateToAgreement : SettingsCommand()
}

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(application) }

    private val stateLiveData = MutableLiveData(
        SettingsUiState((application.resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
    )
    fun observeState(): LiveData<SettingsUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<SettingsCommand>()
    fun observeCommand(): LiveData<SettingsCommand> = commandLiveData

    fun onChangeTheme(isNight: Boolean) {
        stateLiveData.postValue(stateLiveData.value?.copy(isNightMode = isNight))
        settingsInteractor.saveTheme(isNight)
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun onShareClick() {
        commandLiveData.postValue(SettingsCommand.NavigateToShare)
    }

    fun onSupportClick() {
        commandLiveData.postValue(SettingsCommand.NavigateToSupport)
    }

    fun onAgreementClick() {
        commandLiveData.postValue(SettingsCommand.NavigateToAgreement)
    }
}