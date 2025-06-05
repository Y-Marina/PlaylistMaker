package com.hfad.playlistmaker.domian.db

import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.domian.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun getPlaylist(): Flow<List<Playlist>>
}