package com.hfad.playlistmaker.data.network

import com.hfad.playlistmaker.data.NetworkClient
import com.hfad.playlistmaker.data.dto.MusicRequest
import com.hfad.playlistmaker.data.dto.Response

class RetrofitNetworkClient(
    private  val iTunesService: ITunesApi
) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (dto is MusicRequest) {
            val resp = iTunesService.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode == 400 }
        }
    }
}
