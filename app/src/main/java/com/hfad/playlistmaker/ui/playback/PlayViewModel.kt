package com.hfad.playlistmaker.ui.playback

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.domian.search.api.HistoryInteractor

data class PlayUiState(
    val track: Track? = null,
    val currentTime: Int = 0,
    val isPlayButtonEnabled: Boolean = false
)

sealed class PlayCommand {
    data object NavigateBack : PlayCommand()
}

enum class PlayState{
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}

class PlayViewModel(
    private val historyInteractor: HistoryInteractor,
    private val mediaPlayer: MediaPlayer
): ViewModel() {
    companion object {
        private const val TIMER_DEBOUNCE = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private var playerState = PlayState.STATE_DEFAULT

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
            PlayState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayState.STATE_PREPARED, PlayState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayState.STATE_DEFAULT -> {
                Unit
            }
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateLiveData.postValue(stateLiveData.value?.copy(isPlayButtonEnabled = true))
            playerState = PlayState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayState.STATE_PREPARED
            handler.removeCallbacks(playTimeRunnable)
            stateLiveData.postValue(stateLiveData.value?.copy(currentTime = 0))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayState.STATE_PLAYING
        handler.postDelayed(playTimeRunnable, TIMER_DEBOUNCE)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayState.STATE_PAUSED
        handler.removeCallbacks(playTimeRunnable)
    }

    fun setTrack(trackId: Long) {
        val track = if (trackId == -1L) {
            null
        } else {
            historyInteractor.getTrackById(trackId = trackId)
        }
        if (playerState == PlayState.STATE_DEFAULT) {
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
