package com.hfad.playlistmaker.domian.storage

interface PlaylistImageRepository {
    suspend fun uploadImage(filePath: String): String

    suspend fun getImage(fileName: String?): String?
}
