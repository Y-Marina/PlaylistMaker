package com.hfad.playlistmaker.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.db.FavTracksInteractor
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.search.SearchAdapter
import com.hfad.playlistmaker.ui.search.SearchItemUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

data class FavUiState(
    val favTracks: List<Track> = emptyList()
) {
    val items = favTracks.map { track ->
        SearchItemUiModel.Item(track)
    }
}

sealed class FavCommand {
    data class NavigateToPlayer(val track: Track) : FavCommand()
}

class MediaFavViewModel(
    private val favTracksInteractor: FavTracksInteractor
) : ViewModel(), SearchAdapter.Callback {
    private val stateLiveData = MutableLiveData(FavUiState())
    fun observeState(): LiveData<FavUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<FavCommand>()
    fun observeCommand(): LiveData<FavCommand> = commandLiveData

    init {
        viewModelScope.launch {
            favTracksInteractor.getFavTracks()
                .distinctUntilChanged()
                .collect { tracks ->
                    stateLiveData.postValue(stateLiveData.value?.copy(favTracks = tracks))
                }
        }

    }

    override fun onItemClick(item: SearchItemUiModel.Item) {
        commandLiveData.postValue(FavCommand.NavigateToPlayer(track = item.track))
    }
}
