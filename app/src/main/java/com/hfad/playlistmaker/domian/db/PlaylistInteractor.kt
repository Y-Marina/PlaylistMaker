package com.hfad.playlistmaker.domian.db

import com.hfad.playlistmaker.domian.models.Playlist

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
}