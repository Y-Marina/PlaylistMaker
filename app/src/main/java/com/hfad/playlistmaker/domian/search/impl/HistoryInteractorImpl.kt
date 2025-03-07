package com.hfad.playlistmaker.domian.search.impl

import com.hfad.playlistmaker.data.search.history.HistoryRepository
import com.hfad.playlistmaker.domian.search.api.HistoryInteractor
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