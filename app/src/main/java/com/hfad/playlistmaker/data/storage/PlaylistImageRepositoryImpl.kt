package com.hfad.playlistmaker.data.storage

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import com.hfad.playlistmaker.domian.storage.PlaylistImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistImageRepositoryImpl(
    private val fileDir: File,
    private val contentResolver: ContentResolver
) : PlaylistImageRepository {
    override suspend fun uploadImage(filePath: String): String {
        return withContext(Dispatchers.IO) {
            val fileName = "${UUID.randomUUID()}.jpg"
            val file = File(fileDir, fileName)

            contentResolver.openInputStream(filePath.toUri()).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    BitmapFactory.decodeStream(inputStream)
                        .compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
                }
            }
            fileName
        }
    }

    override suspend fun getImage(fileName: String?): String? {
       if (fileName.isNullOrEmpty()) return null

        return File(fileDir, fileName).absolutePath
    }
}
