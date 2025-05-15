package com.hfad.playlistmaker.data

import androidx.room.withTransaction
import com.hfad.playlistmaker.data.converters.TrackDbConvertor
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

private fun Track.toTrackEntity() = TrackEntity(
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

class FavTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvector: TrackDbConvertor
) : FavTracksRepository {
    private val trackDao = appDatabase.trackDao()

    override suspend fun getFavTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks().map { entities ->
            entities.map { it.toTrack() }
        }.distinctUntilChanged()
    }

    override suspend fun deleteFavTrack(id: Long) {
        appDatabase.withTransaction {
            trackDao.deleteTrack(id)
        }
    }

    override suspend fun addFavTrack(track: Track) {
        appDatabase.withTransaction {
            trackDao.insertTrack(track.toTrackEntity())
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { tracks -> trackDbConvector.map(tracks) }
    }
}