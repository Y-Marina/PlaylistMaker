package com.hfad.playlistmaker

import android.app.Application
import com.hfad.playlistmaker.di.dataModule
import com.hfad.playlistmaker.di.interactorModule
import com.hfad.playlistmaker.di.repositoryModule
import com.hfad.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}
