package com.hfad.playlistmaker.ui.playlist

import com.hfad.playlistmaker.domian.models.PlaylistWithTracks

sealed class PlaylistItemUiModel {
    abstract val playlist: PlaylistWithTracks

    data class BigPlaylistItem(
        override val playlist: PlaylistWithTracks
    ) : PlaylistItemUiModel() {
        val photoUrl = playlist.photoUrl
        val name = playlist.name
        val trackCount = playlist.tracks.size
    }

    data class SmallPlaylistItem(
        override val playlist: PlaylistWithTracks
    ) : PlaylistItemUiModel() {
        val photoUrl = playlist.photoUrl
        val name = playlist.name
        val trackCount = playlist.tracks.size
    }
}
