package com.hfad.playlistmaker.domian.api

import com.hfad.playlistmaker.domian.models.Track

interface HistoryInteractor {
    fun getAllTrack(consumer: HistoryConsumer)

    fun addTrack(track: Track)

    fun clear()

    interface HistoryConsumer {
        fun consume(trackList: List<Track>)
    }
}