package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.playlistmaker.domian.models.Playlist

data class PlaylistUiState(
    val playlists: List<Playlist> = listOf(
        Playlist("1fjjrl", "jw[fc[ax mdweof opcp", "http://s1.iconbird.com/ico/2013/11/491/w256h2561384698911Chinesegoosebeery.png"),
        Playlist("aeeo[ac", "okweor wpe", "http://s1.iconbird.com/ico/2013/11/491/w256h2561384698911pumpkin.png"),
        Playlist("1fjjrl", "jw[fc[ax mdweof opcp", ""),
        Playlist("kvlx", "ohro oifh", "http://s1.iconbird.com/ico/2013/11/491/w256h2561384698911Chinesegoosebeery.png"),
        Playlist("tueiw", "eohhoeq gbcnm,", "http://s1.iconbird.com/ico/2013/11/491/w256h2561384698911tomato.png")
    )
) {
    val items = playlists.map { playlist ->
        PlaylistItemUiModel.BigPlaylistItem(playlist)
    }

    val isEmptyViewVisible by lazy { playlists.isEmpty() }
}

sealed class PlaylistCommand() {
    data object NavigateToNewPlaylist : PlaylistCommand()
}

class MediaPlaylistViewModel: ViewModel(), PlaylistAdapter.Callback {
    private val stateLiveData = MutableLiveData(PlaylistUiState())
    fun observeState(): LiveData<PlaylistUiState> = stateLiveData

    override fun onItemClick(item: PlaylistItemUiModel) {

    }
}
