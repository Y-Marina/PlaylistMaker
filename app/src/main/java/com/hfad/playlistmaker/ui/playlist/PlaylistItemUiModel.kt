package com.hfad.playlistmaker.ui.playlist

import com.hfad.playlistmaker.domian.models.Playlist

sealed class PlaylistItemUiModel {
    abstract val playlist: Playlist

    data class BigPlaylistItem(
        override val playlist: Playlist
    ) : PlaylistItemUiModel() {
        val photoUrl = playlist.photoUrl
        val name = playlist.name
        val trackCount = 10
    }

    data class SmallPlaylistItem(
        override val playlist: Playlist
    ) : PlaylistItemUiModel() {
        val photoUrl = playlist.photoUrl
        val name = playlist.name
        val trackCount = 10
    }
}

