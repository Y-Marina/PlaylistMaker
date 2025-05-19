package com.hfad.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.playlistmaker.data.db.dao.TrackDao
import com.hfad.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}