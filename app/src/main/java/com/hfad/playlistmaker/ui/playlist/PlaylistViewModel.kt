package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.search.SearchAdapter
import com.hfad.playlistmaker.ui.search.SearchItemUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

data class GetPlaylistUiState(
    val playlistWithTracks: PlaylistWithTracks? = null
) {
    val trackItem by lazy {
        playlistWithTracks?.tracks?.map {
            SearchItemUiModel.Item(it)
        } ?: emptyList()
    }
}

sealed class PlaylistCommand {
    data object NavigateBack : PlaylistCommand()
    data class NavigateToPlayer(val track: Track) : PlaylistCommand()
    data class ShowDeleteTrackDialog(val track: Track) : PlaylistCommand()
    data class NavigateToShare(val playlistWithTracks: PlaylistWithTracks) : PlaylistCommand()
    data object ShowToast : PlaylistCommand()
    data class NavigateToMenu(val playlistName: String) : PlaylistCommand()
    data object NavigateToMediaFragment: PlaylistCommand()
}

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel(), SearchAdapter.Callback {
    private val stateLiveData = MutableLiveData(GetPlaylistUiState())
    fun observeState(): LiveData<GetPlaylistUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<PlaylistCommand>()
    fun observeCommand(): LiveData<PlaylistCommand> = commandLiveData

    fun onBackClicked() {
        commandLiveData.postValue(PlaylistCommand.NavigateBack)
    }

    fun setPlaylistName(playlistName: String?) {
        if (playlistName != null) {
            viewModelScope.launch {
                playlistInteractor.getPlaylistByName(playlistName)
                    .distinctUntilChanged()
                    .collect { playlistWithTracks ->
                        if (playlistWithTracks != null) {
                            stateLiveData.postValue(stateLiveData.value?.copy(playlistWithTracks = playlistWithTracks))
                        } else {
                            commandLiveData.postValue(PlaylistCommand.NavigateBack)
                        }
                    }
            }
        }
    }

    override fun onItemClick(item: SearchItemUiModel.Item) {
        commandLiveData.postValue(PlaylistCommand.NavigateToPlayer(track = item.track))
    }

    override fun onItemLongClick(item: SearchItemUiModel.Item) {
        commandLiveData.postValue(PlaylistCommand.ShowDeleteTrackDialog(item.track))
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            stateLiveData.value?.playlistWithTracks?.playlist?.name?.let { name ->
                playlistInteractor.deleteTrackFromPlaylist(track.trackId, name)
            }
        }
    }

    fun onShareClick() {
        val playlist = stateLiveData.value?.playlistWithTracks
        playlist?.let {
            if (playlist.tracks.isEmpty() == true) {
                commandLiveData.postValue(PlaylistCommand.ShowToast)
            } else {
                commandLiveData.postValue(PlaylistCommand.NavigateToShare(it))
            }
        }
    }

    fun onMenuClick() {
        val playlist = stateLiveData.value?.playlistWithTracks
        playlist?.let {
            commandLiveData.postValue(PlaylistCommand.NavigateToMenu(it.playlist.name))
        }

    }
}
