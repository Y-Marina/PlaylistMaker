package com.hfad.playlistmaker.domian.api

import com.hfad.playlistmaker.domian.models.Track

interface MusicInteractor {
    fun searchTracks(exception: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun onSuccess(foundTracks: List<Track>)

        fun onFailure(exception: Exception)
    }
}