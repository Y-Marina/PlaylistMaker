package com.hfad.playlistmaker.ui.playlist

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.common.getParcelableCompatWrapper
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class CreatePlaylistUiState(
    val name: String = "",
    val description: String? = null,
    val photoUri: Uri? = null
) {
    val isCreateButtonEnabled by lazy {
        name.isNotEmpty()
    }
}

sealed class CreatePlaylistCommand {
    data class NavigateToBackWithSuccess(val result: CreatePlaylistResult) : CreatePlaylistCommand()
    data class NavigateToPlay(val result: AddPlaylistResult) : CreatePlaylistCommand()
    data object ShowWarning : CreatePlaylistCommand()
    data object NavigateToBack : CreatePlaylistCommand()
}

@Serializable
@Parcelize
data class CreatePlaylistResult(
    val playlistName: String
) : Parcelable {
    companion object {
        private val bundleKey = "${CreatePlaylistResult::class.qualifiedName}.bundle"

        fun fromBundle(bundle: Bundle): CreatePlaylistResult {
            val result = bundle.getParcelableCompatWrapper<CreatePlaylistResult>(bundleKey)
            checkNotNull(result) { "Отсутствует значение для $bundleKey." }
            return result
        }
    }

    fun toBundle(): Bundle {
        return bundleOf(bundleKey to this)
    }
}

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData(CreatePlaylistUiState())
    fun observeState(): LiveData<CreatePlaylistUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<CreatePlaylistCommand>()
    fun observeCommand(): LiveData<CreatePlaylistCommand> = commandLiveData

    private var track: Track? = null

    fun setTrack(track: Track?) {
        this.track = track
    }

    fun setPlaylistPhoto(uri: Uri) {
        stateLiveData.postValue(stateLiveData.value?.copy(photoUri = uri))
    }

    fun setPlaylistName(name: String) {
        stateLiveData.postValue(stateLiveData.value?.copy(name = name))
    }

    fun setPlaylistDescription(description: String) {
        stateLiveData.postValue(stateLiveData.value?.copy(description = description))
    }

    fun onCreateButtonClicked() {
        val state = stateLiveData.value

        state?.let { playlist ->
            viewModelScope.launch {
                try {

                    playlistInteractor.addPlaylist(
                        Playlist(
                            name = playlist.name,
                            description = playlist.description,
                            photoUrl = playlist.photoUri?.path
                        )
                    )

                    if (track == null) {
                        commandLiveData.postValue(
                            CreatePlaylistCommand.NavigateToBackWithSuccess(
                                CreatePlaylistResult(
                                    playlist.name
                                )
                            )
                        )
                    } else {
                        track?.let {
                            playlistInteractor.addTrackToPlaylist(
                                it,
                                java.time.Instant.now().epochSecond,
                                playlist.name
                            )
                        }

                        commandLiveData.postValue(
                            CreatePlaylistCommand.NavigateToPlay(
                                AddPlaylistResult(
                                    true,
                                    playlist.name
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun onBackClicked() {
        val uiState = stateLiveData.value
        if (
            uiState?.name?.isEmpty() != true
            || uiState.photoUri != null
            || uiState.description.isNullOrEmpty() != true
        ) {
            commandLiveData.postValue(CreatePlaylistCommand.ShowWarning)
        } else {
            commandLiveData.postValue(CreatePlaylistCommand.NavigateToBack)
        }
    }
}