package com.hfad.playlistmaker.data

import com.hfad.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}