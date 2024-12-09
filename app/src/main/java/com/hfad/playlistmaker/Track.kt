package com.hfad.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    private val trackTime: Long,
    val artworkUrl100: String
) {
    val time
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
}
