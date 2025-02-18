package com.hfad.playlistmaker.domian.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.hfad.playlistmaker.data.history.HistoryRepository
import com.hfad.playlistmaker.domian.models.Track

class HistoryRepositoryImpl(val sharedPreferences: SharedPreferences): HistoryRepository {
    companion object {
        const val LAST_VIEW_KEY = "last_view_key"
    }

    override fun getAllTrack(): List<Track> {
        val json = sharedPreferences.getString(LAST_VIEW_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun addTrack(track: Track) {
        val tracks = getAllTrack()
        val newTracks = tracks.filterNot { it.trackId == track.trackId }.take(9).toMutableList()
        newTracks.add(0, track)
        saveTracks(newTracks)
    }

    override fun clear() {
        saveTracks(emptyList())
    }

    private fun saveTracks(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(LAST_VIEW_KEY, json)
            .apply()
    }
}