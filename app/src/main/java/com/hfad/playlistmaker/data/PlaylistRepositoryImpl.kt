package com.hfad.playlistmaker.data

import androidx.room.withTransaction
import com.hfad.playlistmaker.data.db.AppDatabase
import com.hfad.playlistmaker.data.db.entity.PlaylistEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.hfad.playlistmaker.data.db.entity.PlaylistWithTracksEntity
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private fun PlaylistWithTracksEntity.toPlaylistWithTracks() = PlaylistWithTracks(
    playlist = playlistEntity.toPlaylist(),
    tracks = tracks.sortedByDescending { it.addTime }.map { it.toTrack() }
)

private fun PlaylistEntity.toPlaylist() = Playlist(
    id = id,
    name = name,
    description = description,
    photoUrl = photoUrl
)

private fun Playlist.toPlaylistEntity() = PlaylistEntity(
    id = id,
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

private fun Track.toPlaylistTrackEntity(time: Long, playlistId: Long) = PlaylistTrackEntity(
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
    playlistId = playlistId
)

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase
) : PlaylistRepository {
    private val playlistDao = appDatabase.playlistDao()
    private val playlistTrackDao = appDatabase.playlistTrackDao()

    override suspend fun addPlaylist(playlist: Playlist): Playlist? {
        return appDatabase.withTransaction {
            playlistDao.insertPlaylist(playlist.toPlaylistEntity())
            return@withTransaction playlistDao.getPlaylistByName(playlist.name).firstOrNull()?.toPlaylist()
        }
    }

    override suspend fun getPlaylistById(id: Long): Flow<PlaylistWithTracks?> {
        return appDatabase.playlistDao().getPlaylistById(id).map { it?.toPlaylistWithTracks() }
    }

    override suspend fun getPlaylistByName(name: String): Playlist? {
        return appDatabase.playlistDao().getPlaylistByName(name).firstOrNull()?.toPlaylist()
    }

    override suspend fun getAllPlaylists(): Flow<List<PlaylistWithTracks>> {
        return appDatabase.playlistDao()
            .getPlaylist().map { entities ->
                entities.map { it.toPlaylistWithTracks() }
            }
            .distinctUntilChanged()
    }

    override suspend fun addTrackToPlaylist(track: Track, time: Long, playlistId: Long) {
        appDatabase.withTransaction {
            val entity = track.toPlaylistTrackEntity(time, playlistId)
            playlistTrackDao.addTrackToPlaylist(entity)
        }
    }

    override suspend fun getTrackFromPlaylist(
        trackId: Long,
        playlistId: Long
    ): List<PlaylistTrackEntity> {
        return appDatabase.playlistTrackDao().getTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        return appDatabase.playlistTrackDao().deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.withTransaction {
            playlistTrackDao.deleteTrackFromPlaylist(playlistId)
            playlistDao.deletePlaylist(playlistId)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlist.toPlaylistEntity())
    }
}
