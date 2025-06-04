package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.domian.db.FavTracksInteractor
import com.hfad.playlistmaker.domian.db.FavTracksRepository
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

class FavTracksInteractorImpl(
    private val favTracksRepository: FavTracksRepository
) : FavTracksInteractor {
    override suspend fun getFavTracks(): Flow<List<Track>> {
        return favTracksRepository.getFavTracks()
    }

    override suspend fun deleteFavTrack(id: Long) {
        return favTracksRepository.deleteFavTrack(id)
    }

    override suspend fun addFavTrack(track: Track, time: Long) {
        favTracksRepository.addFavTrack(track, time)
    }

    override suspend fun getFavTrack(id: Long): List<Track> {
        return favTracksRepository.getFavTrack(id)
    }
}