package com.hfad.playlistmaker.data.storage

import com.hfad.playlistmaker.domian.models.Track

interface LocalStorage {
    fun getAllLocalTrack(): List<Track>

    fun addTrack(track: Track)

    fun clear()

    fun hasSavedTheme(): Boolean

    fun saveTheme(isNight: Boolean)

    fun getTheme(): Boolean
}