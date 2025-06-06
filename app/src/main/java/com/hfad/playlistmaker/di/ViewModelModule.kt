package com.hfad.playlistmaker.di

import com.hfad.playlistmaker.ui.main.MainViewModel
import com.hfad.playlistmaker.ui.playback.PlayViewModel
import com.hfad.playlistmaker.ui.playlist.MediaFavViewModel
import com.hfad.playlistmaker.ui.playlist.MediaPlaylistViewModel
import com.hfad.playlistmaker.ui.search.SearchViewModel
import com.hfad.playlistmaker.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        PlayViewModel(get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        MediaFavViewModel(get())
    }

    viewModel {
        MediaPlaylistViewModel()
    }

    viewModel {
        SettingsViewModel(get())
    }
}
