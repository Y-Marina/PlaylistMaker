package com.hfad.playlistmaker.ui.playlist

import com.hfad.playlistmaker.domian.models.PlaylistWithTracks

sealed class PlaylistItemUiModel {
    abstract val playlistWithTracks: PlaylistWithTracks

    data class BigPlaylistItem(
        override val playlistWithTracks: PlaylistWithTracks
    ) : PlaylistItemUiModel() {
        val photoUrl = playlistWithTracks.playlist.photoUrl
        val name = playlistWithTracks.playlist.name
        val trackCount = playlistWithTracks.tracks.size
    }

    data class SmallPlaylistItem(
        override val playlistWithTracks: PlaylistWithTracks
    ) : PlaylistItemUiModel() {
        val photoUrl = playlistWithTracks.playlist.photoUrl
        val name = playlistWithTracks.playlist.name
        val trackCount = playlistWithTracks.tracks.size
    }
}
