package com.hfad.playlistmaker.data

import com.hfad.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
