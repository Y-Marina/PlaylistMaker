package com.hfad.playlistmaker.domian.api

import com.hfad.playlistmaker.common.Resource
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun searchMusic(expression: String): Flow<Resource<List<Track>>>
}
