package com.hfad.playlistmaker.data.db.entity

import androidx.room.Relation

data class PlaylistWithTracksEntity(

    val name: String,

    val description: String?,

    val photoUrl: String?,

    @Relation(parentColumn = "name", entityColumn = "playlist_name")
    val tracks: List<PlaylistTrackEntity>
)
