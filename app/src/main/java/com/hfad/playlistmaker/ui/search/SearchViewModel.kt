package com.hfad.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.domian.search.api.HistoryInteractor

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val findTracks: List<Track> = emptyList(),
    val historyTracks: List<Track> = emptyList(),
    val isError: Boolean = false
) {
    val isEmptyResult by lazy {
        !isLoading && !isError && searchText.isNotEmpty() && findTracks.isEmpty()
    }

    val isContentVisible by lazy {
        !isLoading && !isError && !isEmptyResult
    }

    val items = buildList {
        if (findTracks.isNotEmpty()) {
            addAll(findTracks.map { SearchItemUiModel.Item(it) })
        } else {
            if (historyTracks.isNotEmpty() && searchText.isEmpty()) {
                add(SearchItemUiModel.Header)
                addAll(historyTracks.map { SearchItemUiModel.Item(it) })
                add(SearchItemUiModel.Button)
            } else {
                emptyList<SearchItemUiModel>()
            }
        }
    }
}

sealed class SearchCommand {
    data class NavigateToPlayer(val trackId: Long) : SearchCommand()
}

class SearchViewModel(
    private val musicInteractor: MusicInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel(),
    SearchAdapter.Callback {

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    private var latestSearchText = ""

    private val searchRunnable = Runnable { search() }

    private val stateLiveData = MediatorLiveData(SearchUiState()).also { liveData ->
        liveData.addSource(historyInteractor.observeHistoryState()) { historyState ->
            liveData.postValue(liveData.value?.copy(historyTracks = historyState))
        }
    }

    fun observeState(): LiveData<SearchUiState> {
        return stateLiveData
    }

    private val commandLiveData = SingleLiveEvent<SearchCommand>()
    fun observeCommand(): LiveData<SearchCommand> = commandLiveData

    init {
        historyInteractor.getAllTrack(object : HistoryInteractor.HistoryConsumer {
            override fun consume(trackList: List<Track>) {
                stateLiveData.postValue(stateLiveData.value?.copy(historyTracks = trackList))
            }
        })
    }

    fun searchDebounce(changedText: String) {
        if (changedText.isEmpty()) {
            latestSearchText = ""
            stateLiveData.postValue(
                stateLiveData.value?.copy(
                    searchText = "",
                    findTracks = emptyList()
                )
            )
            return
        }

        if (latestSearchText == changedText) {
            return
        } else {
            latestSearchText = changedText
            handler.removeCallbacksAndMessages(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun onClearClicked() {
        stateLiveData.postValue(
            stateLiveData.value?.copy(
                searchText = "",
                findTracks = emptyList()
            )
        )
    }

    fun search() {
        if (latestSearchText.isNotEmpty()) {

            stateLiveData.postValue(
                stateLiveData.value?.copy(
                    isLoading = true,
                    searchText = latestSearchText,
                    isError = false
                )
            )

            musicInteractor.searchTracks(latestSearchText, object : MusicInteractor.TracksConsumer {
                override fun onSuccess(tracks: List<Track>) {
                    stateLiveData.postValue(
                        stateLiveData.value?.copy(
                            isLoading = false,
                            findTracks = tracks,
                            isError = false
                        )
                    )
                }

                override fun onFailure(exception: Exception) {
                    stateLiveData.postValue(
                        stateLiveData.value?.copy(
                            isLoading = false,
                            isError = true
                        )
                    )
                }
            })
        }
    }

    override fun onItemClick(item: SearchItemUiModel.Item) {
        clickDebounce()
        historyInteractor.addTrack(item.track)
        commandLiveData.postValue(SearchCommand.NavigateToPlayer(trackId = item.track.trackId))
    }

    override fun onClearHistoryClick() {
        historyInteractor.clear()
    }
}