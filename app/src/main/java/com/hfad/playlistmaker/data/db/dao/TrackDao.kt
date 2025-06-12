package com.hfad.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hfad.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavTrack(track: TrackEntity)

    @Query("SELECT * FROM fav_track_table")
    fun getFavTracks(): Flow<List<TrackEntity>>

    @Query("DELETE FROM fav_track_table WHERE track_id = :id")
    suspend fun deleteFavTrack(id: Long)

    @Query("SELECT * FROM fav_track_table WHERE track_id = :id")
    suspend fun getFavTrackById(id: Long): List<TrackEntity>
}