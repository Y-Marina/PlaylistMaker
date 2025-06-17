package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.Playlist
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
    data object NavigateToBack: MenuCommand()
    data class NavigateToShare(val playlistWithTracks: PlaylistWithTracks) : MenuCommand()
    data object ShowToast : MenuCommand()
    data class NavigateToChangeInfo(val playlistId: Long) : MenuCommand()
    data object NavigateToDeletePlaylist : MenuCommand()
}

class MenuBottomSheetViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData(MenuBottomSheetUiState())
    fun observeState(): LiveData<MenuBottomSheetUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<MenuCommand>()
    fun observeCommand(): LiveData<MenuCommand> = commandLiveData

    fun setPlaylistId(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId)
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

    fun onEditClicked() {
        val playlist = stateLiveData.value?.playlistWithTracks
        playlist?.let {
            commandLiveData.postValue(MenuCommand.NavigateToChangeInfo(it.playlist.id))
        }
    }

    fun onDeleteMenuClicked() {
        commandLiveData.postValue(MenuCommand.NavigateToDeletePlaylist)
    }

    fun onDeletePlaylist() {
        commandLiveData.postValue(MenuCommand.NavigateToBack)
        viewModelScope.launch {
            stateLiveData.value?.playlistWithTracks?.playlist?.id?.let {
                playlistInteractor.deletePlaylist(it)
            }
        }
    }
}
