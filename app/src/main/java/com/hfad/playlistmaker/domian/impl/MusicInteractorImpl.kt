package com.hfad.playlistmaker.domian.impl

import java.util.concurrent.Executors
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.api.MusicRepository

class MusicInteractorImpl(private val repository: MusicRepository) : MusicInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(exception: String, consumer: MusicInteractor.TracksConsumer) {
        executor.execute {
            try {
                consumer.onSuccess(repository.searchMusic(exception))
            } catch (exception: Exception) {
                consumer.onFailure(exception)
            }

        }
    }
}