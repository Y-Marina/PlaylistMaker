package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun getPlaylist(): Flow<List<PlaylistWithTracks>> {
        return playlistRepository.getPlaylist()
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
