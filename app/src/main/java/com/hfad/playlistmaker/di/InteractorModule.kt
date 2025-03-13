package com.hfad.playlistmaker.di

import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.impl.MusicInteractorImpl
import com.hfad.playlistmaker.domian.search.api.HistoryInteractor
import com.hfad.playlistmaker.domian.search.impl.HistoryInteractorImpl
import com.hfad.playlistmaker.domian.settings.api.SettingsInteractor
import com.hfad.playlistmaker.domian.settings.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<MusicInteractor> {
        MusicInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}
