package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

data class MenuBottomSheetUiState(
    val playlistWithTracks: PlaylistWithTracks? = null
) {
    val name = playlistWithTracks?.playlist?.name ?: ""
    val trackCount = playlistWithTracks?.trackCount ?: 0
    val photoUrl = playlistWithTracks?.playlist?.photoUrl
}

sealed class MenuCommand {
    data class NavigateToShare(val playlistWithTracks: PlaylistWithTracks) : MenuCommand()
    data object ShowToast : MenuCommand()
    data object NavigateToChangeInfo : MenuCommand()
    data object NavigateToDeletePlaylist : MenuCommand()
}

class MenuBottomSheetViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData(MenuBottomSheetUiState())
    fun observeState(): LiveData<MenuBottomSheetUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<MenuCommand>()
    fun observeCommand(): LiveData<MenuCommand> = commandLiveData

    fun setPlaylist(playlistName: String) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistByName(playlistName)
                .distinctUntilChanged()
                .collect { playlistWithTracks ->
                    stateLiveData.postValue(stateLiveData.value?.copy(playlistWithTracks = playlistWithTracks))
                }
        }
    }

    fun onShareClick() {
        val playlist = stateLiveData.value?.playlistWithTracks
        playlist?.let {
            if (playlist.tracks.isEmpty() == true) {
                commandLiveData.postValue(MenuCommand.ShowToast)
            } else {
                commandLiveData.postValue(MenuCommand.NavigateToShare(it))
            }
        }
    }
}
