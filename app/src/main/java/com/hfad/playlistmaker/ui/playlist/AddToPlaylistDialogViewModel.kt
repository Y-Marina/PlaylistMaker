package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.Playlist
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

data class AddPlaylistUiState(
    val playlists: List<Playlist> = emptyList()
) {
    val items = playlists.map { playlist ->
        PlaylistItemUiModel.SmallPlaylistItem(playlist)
    }
}

class AddToPlaylistDialogViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel(), PlaylistAdapter.Callback {
    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylist()
                .distinctUntilChanged()
                .collect { playlists ->
                    playlistStateLiveData
                        .postValue(playlistStateLiveData.value?.copy(playlists = playlists))
                }
        }
    }

    private val playlistStateLiveData = MutableLiveData(AddPlaylistUiState())
    fun observePlaylistState(): LiveData<AddPlaylistUiState> = playlistStateLiveData

    override fun onItemClick(item: PlaylistItemUiModel) {

    }
}