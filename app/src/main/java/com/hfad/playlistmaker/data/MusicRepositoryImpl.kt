package com.hfad.playlistmaker.data

import com.hfad.playlistmaker.common.Resource
import com.hfad.playlistmaker.data.dto.MusicRequest
import com.hfad.playlistmaker.data.dto.MusicResponse
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MusicRepositoryImpl(private val networkClient: NetworkClient) : MusicRepository {
    override fun searchMusic(expression: String): Flow<Resource<List<Track>>> {
        return flow {
            val response = networkClient.doRequest(MusicRequest(expression))
            if (response.resultCode == 200) {
                val data = (response as MusicResponse).results.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTime = it.trackTime,
                        artworkUrl100 = it.artworkUrl100,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                }
                emit(Resource.Success(data))
            } else {
                emit(Resource.Error("ошибка сервера"))
            }
        }
    }
}
