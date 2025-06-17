package com.hfad.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistWithTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT id, name, description, photo_url FROM playlist_table")
    fun getPlaylist(): Flow<List<PlaylistWithTracksEntity>>

    @Transaction
    @Query("SELECT id, name, description, photo_url FROM playlist_table WHERE id = :id LIMIT 1")
    fun getPlaylistById(id: Long): Flow<PlaylistWithTracksEntity?>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE name = :name LIMIT 1")
    fun getPlaylistByName(name: String): List<PlaylistEntity>

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    fun deletePlaylist(playlistId: Long)

//    @Query("UPDATE FROM playlist_table WHERE name = :name, description = :description, ")
//    fun updatePlaylist(name: String, descriptor: String, photoUrl: String)
}
