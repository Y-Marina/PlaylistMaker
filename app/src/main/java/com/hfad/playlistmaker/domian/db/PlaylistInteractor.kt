package com.hfad.playlistmaker.domian.db

import android.net.Uri
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist, photoUrl: String?)

    suspend fun getAllPlaylists(): Flow<List<PlaylistWithTracks>>

    suspend fun addTrackToPlaylist(track: Track, time: Long, playlistName: String)

    suspend fun getTrackFromPlaylist(trackId: Long, playlistName: String): List<PlaylistTrackEntity>
}
