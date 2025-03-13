package com.hfad.playlistmaker.di

import com.hfad.playlistmaker.data.MusicRepositoryImpl
import com.hfad.playlistmaker.data.search.history.HistoryRepository
import com.hfad.playlistmaker.data.settings.SettingsRepository
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.search.impl.HistoryRepositoryImpl
import com.hfad.playlistmaker.domian.settings.impl.SettingsRepositoryImpl
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
}
