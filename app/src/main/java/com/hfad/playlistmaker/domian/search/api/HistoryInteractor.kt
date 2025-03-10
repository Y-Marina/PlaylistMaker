package com.hfad.playlistmaker.domian.search.api

import androidx.lifecycle.LiveData
import com.hfad.playlistmaker.domian.models.Track

interface HistoryInteractor {
    fun observeHistoryState(): LiveData<List<Track>>

    fun getAllTrack(consumer: HistoryConsumer)

    fun getTrackById(trackId: Long): Track?

    fun addTrack(track: Track)

    fun clear()

    interface HistoryConsumer {
        fun consume(trackList: List<Track>)
    }
}