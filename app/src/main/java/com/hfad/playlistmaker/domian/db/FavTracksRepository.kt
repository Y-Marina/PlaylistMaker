package com.hfad.playlistmaker.domian.db

import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksRepository {
    suspend fun getFavTracks(): Flow<List<Track>>

    suspend fun deleteFavTrack(id: Long)

    suspend fun addFavTrack(track: Track)
}