package com.hfad.playlistmaker.di

import com.hfad.playlistmaker.ui.main.MainViewModel
import com.hfad.playlistmaker.ui.playback.PlayViewModel
import com.hfad.playlistmaker.ui.playlist.AddToPlaylistDialogViewModel
import com.hfad.playlistmaker.ui.playlist.CreatePlaylistViewModel
import com.hfad.playlistmaker.ui.playlist.MediaViewModel
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
        MediaViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel {
        AddToPlaylistDialogViewModel(get())
    }
}
