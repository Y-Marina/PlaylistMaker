package com.hfad.playlistmaker.data

import androidx.room.withTransaction
import com.hfad.playlistmaker.data.db.AppDatabase
import com.hfad.playlistmaker.data.db.entity.TrackEntity
import com.hfad.playlistmaker.domian.db.FavTracksRepository
import com.hfad.playlistmaker.domian.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private fun TrackEntity.toTrack() = Track(
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

private fun Track.toTrackEntity(time: Long) = TrackEntity(
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
    addTime = time
)

class FavTracksRepositoryImpl(
    private val appDatabase: AppDatabase
) : FavTracksRepository {
    private val trackDao = appDatabase.trackDao()

    override suspend fun getFavTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getFavTracks().map { entities ->
            entities.sortedByDescending { it.addTime }.map { it.toTrack() }
        }.distinctUntilChanged()
    }

    override suspend fun deleteFavTrack(id: Long) {
        appDatabase.withTransaction {
            trackDao.deleteFavTrack(id)
        }
    }

    override suspend fun addFavTrack(track: Track, time: Long) {
        appDatabase.withTransaction {
            trackDao.insertFavTrack(track.toTrackEntity(time))
        }
    }

    override suspend fun getFavTrack(id: Long): List<Track> {
        return trackDao.getFavTrackById(id).map { it.toTrack() }
    }
}
