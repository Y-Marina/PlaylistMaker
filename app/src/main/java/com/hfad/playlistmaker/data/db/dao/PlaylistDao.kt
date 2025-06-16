package com.hfad.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistWithTracksEntity
import com.hfad.playlistmaker.domian.models.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT name, description, photo_url FROM playlist_table")
    fun getPlaylist(): Flow<List<PlaylistWithTracksEntity>>

    @Transaction
    @Query("SELECT name, description, photo_url FROM playlist_table WHERE name = :name LIMIT 1")
    fun getPlaylistByName(name: String): Flow<PlaylistWithTracksEntity?>

    @Query("DELETE FROM playlist_table WHERE name = :playlistName")
    fun deletePlaylist(playlistName: String)
}
