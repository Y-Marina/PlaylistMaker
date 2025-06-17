package com.hfad.playlistmaker.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithTracksEntity(
    @Embedded
    val playlistEntity: PlaylistEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "playlist_id",
        entity = PlaylistTrackEntity::class
    )
    val tracks: List<PlaylistTrackEntity>
)
