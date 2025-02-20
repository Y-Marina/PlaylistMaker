package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.data.history.HistoryRepository
import com.hfad.playlistmaker.domian.api.HistoryInteractor
import com.hfad.playlistmaker.domian.models.Track

class HistoryInteractorImpl(val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun getAllTrack(consumer: HistoryInteractor.HistoryConsumer) {
        consumer.consume(historyRepository.getAllTrack())
    }

    override fun addTrack(track: Track) {
        historyRepository.addTrack(track)
    }

    override fun clear() {
        historyRepository.clear()
    }
}