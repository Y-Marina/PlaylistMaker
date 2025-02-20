package com.hfad.playlistmaker.domian.api

import com.hfad.playlistmaker.domian.models.Track

interface MusicRepository {
    fun searchMusic(expression: String): List<Track>
}