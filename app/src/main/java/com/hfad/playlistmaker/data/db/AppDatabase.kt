package com.hfad.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.playlistmaker.data.db.dao.PlaylistDao
import com.hfad.playlistmaker.data.db.dao.PlaylistTrackDao
import com.hfad.playlistmaker.data.db.dao.TrackDao
import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.data.db.entity.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}