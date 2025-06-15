package com.hfad.playlistmaker.domian.models

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
): Parcelable {
    @IgnoredOnParcel
    val time by lazy {
        (tracks.sumOf { it.trackTime } / 1000 / 60).toInt()
    }

    @IgnoredOnParcel
    val trackCount by lazy {
        tracks.size
    }
}
