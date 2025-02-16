package com.hfad.playlistmaker

import com.hfad.playlistmaker.data.MusicRepositoryImpl
import com.hfad.playlistmaker.data.network.RetrofitNetworkClient
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.api.MusicRepository
import com.hfad.playlistmaker.domian.impl.MusicInteractorImpl

object Creator {
    private fun getMusicRepository(): MusicRepository {
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }

     fun provideMusicInteractor(): MusicInteractor {
        return MusicInteractorImpl(getMusicRepository())
    }
}