package com.hfad.playlistmaker.data.search.history

import androidx.lifecycle.LiveData
import com.hfad.playlistmaker.domian.models.Track

interface HistoryRepository {
    fun observeHistoryState(): LiveData<List<Track>>

    fun getAllTrack(): List<Track>

    fun getTrackById(trackId: Long): Track?

    fun addTrack(track: Track)

    fun clear()
}