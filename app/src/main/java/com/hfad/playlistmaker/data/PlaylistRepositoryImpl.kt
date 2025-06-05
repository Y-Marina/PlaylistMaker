package com.hfad.playlistmaker.data

import androidx.room.withTransaction
import com.hfad.playlistmaker.data.db.AppDatabase
import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private fun PlaylistEntity.toPlaylist() = Playlist(
    name = name,
    description = description,
    photoUrl = photoUrl
)

private fun Playlist.toPlaylistEntity() = PlaylistEntity(
    name = name,
    description = description,
    photoUrl = photoUrl
)

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase
) : PlaylistRepository {
    private val playlistDao = appDatabase.playlistDao()
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.withTransaction {
            playlistDao.insertPlaylist(playlist.toPlaylistEntity())
        }
    }

    override suspend fun getPlaylist(): Flow<List<Playlist>> {
        return appDatabase.playlistDao()
            .getPlaylist().map { entities ->
                entities.map { it.toPlaylist() }
            }.distinctUntilChanged()
    }
}