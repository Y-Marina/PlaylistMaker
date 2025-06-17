package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.PlaylistInteractor
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.common.getParcelableCompatWrapper
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class AddPlaylistUiState(
    val playlists: List<PlaylistWithTracks> = emptyList()
) {
    val items = playlists.map { playlist ->
        PlaylistItemUiModel.SmallPlaylistItem(playlist)
    }
}

@Serializable
@Parcelize
data class AddPlaylistResult(
    val success: Boolean,
    val playlistName: String
) : Parcelable {
    companion object {
        private val bundleKey = "${AddPlaylistResult::class.qualifiedName}.bundle"

        fun fromBundle(bundle: Bundle): AddPlaylistResult {
            val result = bundle.getParcelableCompatWrapper<AddPlaylistResult>(bundleKey)
            checkNotNull(result) { "Отсутствует значение для $bundleKey." }
            return result
        }
    }

    fun toBundle(): Bundle {
        return bundleOf(bundleKey to this)
    }
}

sealed class DialogCommand {
    data class Success(val name: String) : DialogCommand()
    data class Error(val name: String) : DialogCommand()
}

class AddToPlaylistDialogViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel(), PlaylistAdapter.Callback {
    init {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .distinctUntilChanged()
                .collect { playlists ->
                    playlistStateLiveData
                        .postValue(playlistStateLiveData.value?.copy(playlists = playlists))
                }
        }
    }

    private lateinit var track: Track

    fun setTrack(track: Track) {
        this@AddToPlaylistDialogViewModel.track = track
    }

    private val playlistStateLiveData = MutableLiveData(AddPlaylistUiState())
    fun observePlaylistState(): LiveData<AddPlaylistUiState> = playlistStateLiveData

    private val commandLiveData = SingleLiveEvent<DialogCommand>()
    fun observeCommand(): LiveData<DialogCommand> = commandLiveData

    override fun onItemClick(item: PlaylistItemUiModel) {
        viewModelScope.launch {
            val isHasTrack =
                playlistInteractor.getTrackFromPlaylist(
                    track.trackId,
                    item.playlistWithTracks.playlist.id
                )
                    .isNotEmpty()
            if (!isHasTrack) {
                playlistInteractor.addTrackToPlaylist(
                    track,
                    java.time.Instant.now().epochSecond,
                    item.playlistWithTracks.playlist.id
                )
                commandLiveData.postValue(DialogCommand.Success(name = item.playlistWithTracks.playlist.name))
            } else {
                commandLiveData.postValue((DialogCommand.Error(name = item.playlistWithTracks.playlist.name)))
            }
        }
    }
}
