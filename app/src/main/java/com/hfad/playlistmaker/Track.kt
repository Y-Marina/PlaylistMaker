package com.hfad.playlistmaker

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    private val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable {
    fun getTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getYear(): String {
        return releaseDate.subSequence(0, 4).toString()
    }
}
