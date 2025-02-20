package com.hfad.playlistmaker.data

import com.hfad.playlistmaker.data.dto.MusicRequest
import com.hfad.playlistmaker.data.dto.MusicResponse
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.models.Track

class MusicRepositoryImpl(private val networkClient: NetworkClient) : MusicRepository {
    override fun searchMusic(expression: String): List<Track> {
        val response = networkClient.doRequest(MusicRequest(expression))
        if (response.resultCode == 200) {
            return (response as MusicResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}