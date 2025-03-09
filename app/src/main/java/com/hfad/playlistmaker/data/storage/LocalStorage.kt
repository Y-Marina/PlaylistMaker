package com.hfad.playlistmaker.data.storage

import androidx.lifecycle.LiveData
import com.hfad.playlistmaker.domian.models.Track

interface LocalStorage {
    fun observeHistoryState(): LiveData<List<Track>>

    fun getAllLocalTrack(): List<Track>

    fun addTrack(track: Track)

    fun clear()

    fun hasSavedTheme(): Boolean

    fun saveTheme(isNight: Boolean)

    fun getTheme(): Boolean
}