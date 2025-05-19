package com.hfad.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.hfad.playlistmaker.data.LocalStorageImpl
import com.hfad.playlistmaker.data.NetworkClient
import com.hfad.playlistmaker.data.db.AppDatabase
import com.hfad.playlistmaker.data.network.ITunesApi
import com.hfad.playlistmaker.data.network.RetrofitNetworkClient
import com.hfad.playlistmaker.data.storage.LocalStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<LocalStorage> {
        LocalStorageImpl(get(), get())
    }

    single {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}
