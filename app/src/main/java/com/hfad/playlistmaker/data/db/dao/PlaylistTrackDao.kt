package com.hfad.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(track: PlaylistTrackEntity)

    @Insert
    suspend fun addTrackToPlaylist(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun getTrackFromPlaylist(trackId: Long, playlistId: Long): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_track_table WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    @Query("DELETE FROM playlist_track_table WHERE playlist_id = :playlistId")
    suspend fun deleteTrackFromPlaylist(playlistId: Long)
}
