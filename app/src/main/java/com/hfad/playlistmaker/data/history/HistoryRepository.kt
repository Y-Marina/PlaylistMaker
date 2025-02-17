package com.hfad.playlistmaker.data.history

import com.hfad.playlistmaker.domian.models.Track

interface HistoryRepository {
    fun getAllTrack(): Array<Track>

    fun addTrack(track: Track)

    fun clear()
}