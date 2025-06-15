package com.hfad.playlistmaker.domian.impl

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

    override suspend fun getPlaylistByName(name: String): Flow<PlaylistWithTracks> {
        return playlistRepository.getPlaylistByName(name)
    }

    override suspend fun getAllPlaylists(): Flow<List<PlaylistWithTracks>> {
        return playlistRepository.getAllPlaylists().map { listOfPlaylists ->
            listOfPlaylists.map { playlistWithTracks ->
                val photoUrl = playlistWithTracks.playlist.photoUrl?.let {
                    "file://${playlistImageStorage.getImage(it)}"
                }
                playlistWithTracks.copy(
                    playlist = playlistWithTracks.playlist.copy(photoUrl = photoUrl)
                )
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

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistName: String) {
        return playlistRepository.deleteTrackFromPlaylist(trackId, playlistName)
    }
}
