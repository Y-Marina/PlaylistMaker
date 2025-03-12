package com.hfad.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hfad.playlistmaker.data.storage.LocalStorage
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.domian.settings.impl.SettingsRepositoryImpl.Companion.THEME_KEY

class LocalStorageImpl(val sharedPreferences: SharedPreferences) : LocalStorage {
    companion object {
        const val LAST_VIEW_KEY = "last_view_key"
    }

    private val historyState = MutableLiveData<List<Track>>()
    override fun observeHistoryState(): LiveData<List<Track>> {
        return historyState
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == LAST_VIEW_KEY) {
                historyState.postValue(getAllLocalTrack())
            }
        }
    }


    override fun getAllLocalTrack(): List<Track> {
        val json = sharedPreferences.getString(LAST_VIEW_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    override fun getTrackById(trackId: Long): Track? {
        return getAllLocalTrack().firstOrNull { it.trackId == trackId }
    }

    override fun addTrack(track: Track) {
        val tracks = getAllLocalTrack()
        val newTracks = tracks.filterNot { it.trackId == track.trackId }.take(9).toMutableList()
        newTracks.add(0, track)
        saveTracks(newTracks)
    }

    override fun clear() {
        saveTracks(emptyList())
    }

    private fun saveTracks(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit {
            putString(LAST_VIEW_KEY, json)
        }
    }

    override fun hasSavedTheme(): Boolean {
        return sharedPreferences.contains(THEME_KEY)
    }

    override fun saveTheme(isNight: Boolean) {
        sharedPreferences.edit() { putBoolean(THEME_KEY, isNight) }
    }

    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }
}