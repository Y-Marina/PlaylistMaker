package com.hfad.playlistmaker.domian.search.impl

import com.hfad.playlistmaker.data.search.history.HistoryRepository
import com.hfad.playlistmaker.data.storage.LocalStorage
import com.hfad.playlistmaker.domian.models.Track

class HistoryRepositoryImpl(private val localStorage: LocalStorage): HistoryRepository {
    companion object {
        const val LAST_VIEW_KEY = "last_view_key"
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