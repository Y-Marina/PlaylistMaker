package com.hfad.playlistmaker.data.history

import com.hfad.playlistmaker.domian.models.Track

interface HistoryRepository {
    fun getAllTrack(): List<Track>

    fun addTrack(track: Track)

    fun clear()
}