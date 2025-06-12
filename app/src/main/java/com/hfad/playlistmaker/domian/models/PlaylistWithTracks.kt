package com.hfad.playlistmaker.domian.models

data class PlaylistWithTracks(
    val name: String,
    val description: String?,
    val photoUrl: String?,
    val tracks: List<Track>
)
