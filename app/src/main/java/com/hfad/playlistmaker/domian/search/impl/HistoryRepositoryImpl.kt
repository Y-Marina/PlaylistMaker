package com.hfad.playlistmaker.domian.search.impl

import androidx.lifecycle.LiveData
import com.hfad.playlistmaker.data.search.history.HistoryRepository
import com.hfad.playlistmaker.data.storage.LocalStorage
import com.hfad.playlistmaker.domian.models.Track

class HistoryRepositoryImpl(private val localStorage: LocalStorage): HistoryRepository {
    override fun observeHistoryState(): LiveData<List<Track>> {
        return localStorage.observeHistoryState()
    }

    override fun getAllTrack(): List<Track> {
        return localStorage.getAllLocalTrack()
    }

    override fun addTrack(track: Track) {
        localStorage.addTrack(track)
    }

    override fun clear() {
        localStorage.clear()
    }
}