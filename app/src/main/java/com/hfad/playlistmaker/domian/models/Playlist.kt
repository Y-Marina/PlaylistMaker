package com.hfad.playlistmaker.domian.models

data class Playlist(
    val name: String,
    val description: String?,
    val photoUrl: String? = null
)