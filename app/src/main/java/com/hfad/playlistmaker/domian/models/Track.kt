package com.hfad.playlistmaker.domian.models

import android.os.Parcelable
import com.hfad.playlistmaker.ui.common.WarningDialogExtra
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite: Boolean = false
): Parcelable, WarningDialogExtra {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getYear(): String {
        return releaseDate.subSequence(0, 4).toString()
    }
}
