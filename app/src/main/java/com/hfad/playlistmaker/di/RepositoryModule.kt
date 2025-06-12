package com.hfad.playlistmaker.di

import com.hfad.playlistmaker.data.FavTracksRepositoryImpl
import com.hfad.playlistmaker.data.MusicRepositoryImpl
import com.hfad.playlistmaker.data.PlaylistRepositoryImpl
import com.hfad.playlistmaker.data.search.history.HistoryRepository
import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.data.storage.PlaylistImageRepositoryImpl
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.db.FavTracksRepository
import com.hfad.playlistmaker.domian.db.PlaylistRepository
import com.hfad.playlistmaker.domian.search.impl.HistoryRepositoryImpl
import com.hfad.playlistmaker.domian.settings.impl.SettingsRepositoryImpl
import com.hfad.playlistmaker.domian.storage.PlaylistImageRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<MusicRepository> {
        MusicRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), get())
    }

    single<FavTracksRepository> {
        FavTracksRepositoryImpl(get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get())
    }

    single<PlaylistImageRepository> {
        PlaylistImageRepositoryImpl(get(), get())
    }
}
