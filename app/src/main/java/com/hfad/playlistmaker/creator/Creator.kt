package com.hfad.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.hfad.playlistmaker.data.MusicRepositoryImpl
import com.hfad.playlistmaker.data.search.history.HistoryRepository
import com.hfad.playlistmaker.data.network.RetrofitNetworkClient
import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.domian.search.api.HistoryInteractor
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.api.SettingsInteractor
import com.hfad.playlistmaker.domian.search.impl.HistoryInteractorImpl
import com.hfad.playlistmaker.domian.search.impl.HistoryRepositoryImpl
import com.hfad.playlistmaker.domian.impl.MusicInteractorImpl
import com.hfad.playlistmaker.domian.impl.SettingsInteractorImpl
import com.hfad.playlistmaker.domian.impl.SettingsRepositoryImpl

const val PREFERENCES = "preferences"

object Creator {
    private fun getMusicRepository(): MusicRepository {
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }

     fun provideMusicInteractor(): MusicInteractor {
        return MusicInteractorImpl(getMusicRepository())
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(getSharedPreferences(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSharedPreferences(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}