package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }
}