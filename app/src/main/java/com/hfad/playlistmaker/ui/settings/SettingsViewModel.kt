package com.hfad.playlistmaker.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.settings.api.SettingsInteractor

data class SettingsUiState(
    val isNightMode: Boolean = false
)
sealed class SettingsCommand {
    data object NavigateToShare : SettingsCommand()
    data object NavigateToSupport : SettingsCommand()
    data object NavigateToAgreement : SettingsCommand()
}

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
): ViewModel() {
    private val stateLiveData = MutableLiveData(
        SettingsUiState(settingsInteractor.getTheme())
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
