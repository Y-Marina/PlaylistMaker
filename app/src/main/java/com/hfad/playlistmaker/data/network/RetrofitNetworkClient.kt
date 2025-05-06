package com.hfad.playlistmaker.data.network

import com.hfad.playlistmaker.data.NetworkClient
import com.hfad.playlistmaker.data.dto.MusicRequest
import com.hfad.playlistmaker.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesService: ITunesApi
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (dto is MusicRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesService.search(dto.expression)
                    resp.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
