package com.hfad.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(val sharedPreferences: SharedPreferences) {
    companion object {
        const val LAST_VIEW_KEY = "last_view_key"
    }

    fun getAllTrack(): Array<Track> {
        val json = sharedPreferences.getString(LAST_VIEW_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun addTrack(track: Track) {
        val tracks = getAllTrack()
        val newTracks = tracks.filterNot { it.trackId == track.trackId }.take(9).toMutableList()
        newTracks.add(0, track)
        saveTracks(newTracks)
    }

    fun clear() {
        saveTracks(emptyList())
    }

    private fun saveTracks(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(LAST_VIEW_KEY, json)
            .apply()
    }
}