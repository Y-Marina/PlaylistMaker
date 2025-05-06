package com.hfad.playlistmaker.domian.api

import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

interface MusicInteractor {
    fun searchTracks(exception: String): Flow<Pair<List<Track>?, String?>>
}
