package com.hfad.playlistmaker.domian.impl

import android.net.Uri
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.domian.storage.PlaylistImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistImageStorage: PlaylistImageRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist, photoUrl: String?) {
        val fileName = photoUrl?.let { playlistImageStorage.uploadImage(it) }
        playlistRepository.addPlaylist(playlist.copy(photoUrl = fileName))
    }

    override suspend fun getAllPlaylists(): Flow<List<PlaylistWithTracks>> {
        return playlistRepository.getAllPlaylists().map { listOfPlaylists ->
            listOfPlaylists.map { playlist ->
                playlist.copy(photoUrl = playlist.photoUrl?.let {
                    "file://${playlistImageStorage.getImage(playlist.photoUrl)}"
                })
            }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, time: Long, playlistName: String) {
        return playlistRepository.addTrackToPlaylist(track, time, playlistName)
    }

    override suspend fun getTrackFromPlaylist(
        trackId: Long,
        playlistName: String
    ): List<PlaylistTrackEntity> {
        return playlistRepository.getTrackFromPlaylist(trackId, playlistName)
    }
}
