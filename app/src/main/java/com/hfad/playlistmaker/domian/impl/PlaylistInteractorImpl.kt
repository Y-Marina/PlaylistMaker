package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylist()
    }

    override suspend fun addTrackToPlaylist(track: Track, time: Long, playlistName: String) {
        return playlistRepository.addTrackToPlaylist(track, time, playlistName)
    }
}