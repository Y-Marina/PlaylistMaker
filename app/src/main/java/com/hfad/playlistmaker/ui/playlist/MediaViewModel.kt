package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.FavTracksInteractor
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.search.SearchAdapter
import com.hfad.playlistmaker.ui.search.SearchItemUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

data class FavUiState(
    val favTracks: List<Track> = emptyList()
) {
    val items = favTracks.map { track ->
        SearchItemUiModel.Item(track)
    }
}

data class PlaylistUiState(
    val playlists: List<PlaylistWithTracks> = emptyList()
) {
    val items = playlists.map { playlist ->
        PlaylistItemUiModel.BigPlaylistItem(playlist)
    }
}

sealed class MediaCommand {
    data object NavigateToNewPlaylist : MediaCommand()
    data class NavigateToPlayer(val track: Track) : MediaCommand()
    data class NavigateToPlaylist(val playlistName: String) : MediaCommand()
}

class MediaViewModel(
    private val favTracksInteractor: FavTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel(), SearchAdapter.Callback, PlaylistAdapter.Callback {
    init {
        viewModelScope.launch {
            favTracksInteractor.getFavTracks()
                .distinctUntilChanged()
                .collect { tracks ->
                    favStateLiveData.postValue(favStateLiveData.value?.copy(favTracks = tracks))
                }
        }

        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .distinctUntilChanged()
                .collect { playlists ->
                    playlistStateLiveData
                        .postValue(playlistStateLiveData.value?.copy(playlists = playlists))
                }
        }
    }

    private val favStateLiveData = MutableLiveData(FavUiState())
    fun observeFavState(): LiveData<FavUiState> = favStateLiveData

    private val playlistStateLiveData = MutableLiveData(PlaylistUiState())
    fun observePlaylistState(): LiveData<PlaylistUiState> = playlistStateLiveData

    private val commandLiveData = SingleLiveEvent<MediaCommand>()
    fun observeCommand(): LiveData<MediaCommand> = commandLiveData

    override fun onItemClick(item: PlaylistItemUiModel) {
        commandLiveData.postValue(MediaCommand.NavigateToPlaylist(item.playlistWithTracks.playlist.name))
    }

    override fun onItemClick(item: SearchItemUiModel.Item) {
        commandLiveData.postValue(MediaCommand.NavigateToPlayer(track = item.track))
    }
}
