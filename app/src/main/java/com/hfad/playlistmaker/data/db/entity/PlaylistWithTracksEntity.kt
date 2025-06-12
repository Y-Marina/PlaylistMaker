package com.hfad.playlistmaker.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithTracksEntity(
    @Embedded
    val playlistEntity: PlaylistEntity,

    @Relation(
        parentColumn = "name",
        entityColumn = "playlist_name",
        entity = PlaylistTrackEntity::class
    )
    val tracks: List<PlaylistTrackEntity>
)
