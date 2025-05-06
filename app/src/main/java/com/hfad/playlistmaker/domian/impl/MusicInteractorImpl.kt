package com.hfad.playlistmaker.domian.impl

import com.hfad.playlistmaker.common.Resource
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicInteractorImpl(private val repository: MusicRepository) : MusicInteractor {
    override fun searchTracks(exception: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchMusic(exception).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}
