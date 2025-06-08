package com.hfad.playlistmaker.domian.db

import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun getPlaylist(): Flow<List<PlaylistWithTracks>>

    suspend fun addTrackToPlaylist(track: Track, time: Long, playlistName: String)

    suspend fun getTrackFromPlaylist(trackId: Long, playlistName: String): List<PlaylistTrackEntity>
}
