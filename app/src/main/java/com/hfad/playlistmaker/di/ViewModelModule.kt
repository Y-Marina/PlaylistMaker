package com.hfad.playlistmaker.di

import com.hfad.playlistmaker.ui.main.MainViewModel
import com.hfad.playlistmaker.ui.playback.PlayViewModel
import com.hfad.playlistmaker.ui.search.SearchViewModel
import com.hfad.playlistmaker.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(androidApplication(), get())
    }

    viewModel {
        PlayViewModel(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }
}
