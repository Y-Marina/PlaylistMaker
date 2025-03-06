package com.hfad.playlistmaker.ui.playback

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.models.Track

data class PlayUiState(
    val track: Track? = null,
    val currentTime: Int = 0,
    val isPlayButtonEnabled: Boolean = false
)

sealed class PlayCommand {
    data object NavigateBack : PlayCommand()
}

class PlayViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIMER_DEBOUNCE = 1000L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private var playerState = STATE_DEFAULT

    private val mediaPlayer = MediaPlayer()

    private val playTimeRunnable = object : Runnable {
        var currentTime = 0
        override fun run() {
            val t = mediaPlayer.currentPosition
            currentTime = t.coerceAtLeast(currentTime)
            println("myTag t = $t, c = $currentTime")
            stateLiveData.postValue(stateLiveData.value?.copy(currentTime = currentTime))
            handler.postDelayed(this, TIMER_DEBOUNCE)
        }
    }

    private val stateLiveData = MutableLiveData(PlayUiState())
    fun observeState(): LiveData<PlayUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<PlayCommand>()
    fun observeCommand(): LiveData<PlayCommand> = commandLiveData

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }

            STATE_DEFAULT -> {
                Unit
            }
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateLiveData.postValue(stateLiveData.value?.copy(isPlayButtonEnabled = true))
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(playTimeRunnable)
            stateLiveData.postValue(stateLiveData.value?.copy(currentTime = 0))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        handler.postDelayed(playTimeRunnable, TIMER_DEBOUNCE)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        handler.removeCallbacks(playTimeRunnable)
    }

    fun setTrack(track: Track?) {
        if (playerState == STATE_DEFAULT) {
            if (track == null) {
                commandLiveData.postValue(PlayCommand.NavigateBack)
            } else {
                stateLiveData.postValue(stateLiveData.value?.copy(track = track))
                preparePlayer(track)
            }
        } else {
            return
        }
    }

    override fun onCleared() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        super.onCleared()
    }
}