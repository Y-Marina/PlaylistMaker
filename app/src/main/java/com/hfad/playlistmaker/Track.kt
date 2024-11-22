package com.hfad.playlistmaker

data class Track (
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String
) {
    val description: String
        get() = "$artistName • $trackTime"
}
