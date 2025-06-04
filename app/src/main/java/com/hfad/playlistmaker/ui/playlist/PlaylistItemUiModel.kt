package com.hfad.playlistmaker.ui.playlist

import com.hfad.playlistmaker.domian.models.Playlist

sealed class PlaylistItemUiModel {
    data class BigPlaylistItem(
        private val playlistBig: Playlist
    ) : PlaylistItemUiModel() {
        val photoUrl = playlistBig.photoUrl
        val name = playlistBig.name
        val trackCount = 10
    }

    data class SmallPlaylistItem(
        private val playlistSmall: Playlist
    ) : PlaylistItemUiModel() {
        val photoUrl = playlistSmall.photoUrl
        val name = playlistSmall.name
        val trackCount = 10
    }
}

