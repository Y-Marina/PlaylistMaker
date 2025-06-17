package com.hfad.playlistmaker.domian.db

import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist): Playlist?

    suspend fun getPlaylistById(id: Long): Flow<PlaylistWithTracks?>

    suspend fun getPlaylistByName(name: String): Playlist?

    suspend fun getAllPlaylists(): Flow<List<PlaylistWithTracks>>

    suspend fun addTrackToPlaylist(track: Track, time: Long, playlistId: Long)

    suspend fun getTrackFromPlaylist(trackId: Long, playlistId: Long): List<PlaylistTrackEntity>

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun updatePlaylist(playlist: Playlist)
}
