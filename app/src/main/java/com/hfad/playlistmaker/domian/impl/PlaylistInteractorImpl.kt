package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
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
    override suspend fun addPlaylist(playlist: Playlist, photoUrl: String?): Playlist? {
        val fileName = photoUrl?.let { playlistImageStorage.uploadImage(it) }
        return playlistRepository.addPlaylist(playlist.copy(photoUrl = fileName))
    }

    override suspend fun getPlaylistById(id: Long): Flow<PlaylistWithTracks?> {
        return playlistRepository.getPlaylistById(id).map { playlistWithTracks ->
            val photoUrl = getPhotoUrl(playlistWithTracks?.playlist?.photoUrl)
            playlistWithTracks?.copy(
                playlist = playlistWithTracks.playlist.copy(photoUrl = photoUrl)
            )
        }
    }

    override suspend fun getPlaylistByName(name: String): Playlist? {
        return playlistRepository.getPlaylistByName(name)
    }

    override suspend fun getAllPlaylists(): Flow<List<PlaylistWithTracks>> {
        return playlistRepository.getAllPlaylists().map { listOfPlaylists ->
            listOfPlaylists.map { playlistWithTracks ->
                val photoUrl = getPhotoUrl(playlistWithTracks.playlist.photoUrl)
                playlistWithTracks.copy(
                    playlist = playlistWithTracks.playlist.copy(photoUrl = photoUrl)
                )
            }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, time: Long, playlistId: Long) {
        return playlistRepository.addTrackToPlaylist(track, time, playlistId)
    }

    override suspend fun getTrackFromPlaylist(
        trackId: Long,
        playlistId: Long
    ): List<PlaylistTrackEntity> {
        return playlistRepository.getTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        return playlistRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }

    suspend fun getPhotoUrl(fileName: String?): String? {
        return if (fileName != null) {
            "file://${playlistImageStorage.getImage(fileName)}"
        } else {
            null
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist, photoUrl: String?) {
        val fileName = photoUrl?.let { playlistImageStorage.uploadImage(it) }
        playlistRepository.updatePlaylist(playlist.copy(photoUrl = fileName))
    }
}
