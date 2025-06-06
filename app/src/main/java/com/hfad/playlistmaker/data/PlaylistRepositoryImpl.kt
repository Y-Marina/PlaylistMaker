package com.hfad.playlistmaker.data

import androidx.room.withTransaction
import com.hfad.playlistmaker.data.db.AppDatabase
import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.data.db.entity.TrackEntity
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.Track
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

private fun PlaylistTrackEntity.toTrack() = Track(
    trackId = trackId,
    trackName = trackName,
    artistName = artistName,
    trackTime = trackTime,
    artworkUrl100 = artworkUrl100,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl
)

private fun Track.toPlaylistTrackEntity(time: Long, playlistName: String) = PlaylistTrackEntity(
    trackId = trackId,
    trackName = trackName,
    artistName = artistName,
    trackTime = trackTime,
    artworkUrl100 = artworkUrl100,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    addTime = time,
    playlistName = playlistName
)

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase
) : PlaylistRepository {
    private val playlistDao = appDatabase.playlistDao()
    private val playlistTrackDao = appDatabase.playlistTrackDao()

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

    override suspend fun addTrackToPlaylist(track: Track, time: Long, playlistName: String) {
        appDatabase.withTransaction {
            val entity = track.toPlaylistTrackEntity(time, playlistName)
            playlistTrackDao.addTrackToPlaylist(entity)
        }
    }
}