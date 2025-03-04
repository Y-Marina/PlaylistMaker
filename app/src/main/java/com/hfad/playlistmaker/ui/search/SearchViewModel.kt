package com.hfad.playlistmaker.ui.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hfad.playlistmaker.common.SingleLiveEvent
import com.hfad.playlistmaker.creator.Creator
import com.hfad.playlistmaker.creator.PREFERENCES
import com.hfad.playlistmaker.domian.search.api.HistoryInteractor
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.search.impl.HistoryRepositoryImpl
import com.hfad.playlistmaker.domian.models.Track

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
            addAll(findTracks.map { SearchItemViewModel.Item(it) })
        } else {
            if (historyTracks.isNotEmpty() && searchText.isEmpty()) {
                add(SearchItemViewModel.Header)
                addAll(historyTracks.map { SearchItemViewModel.Item(it) })
                add(SearchItemViewModel.Button)
            } else {
                emptyList<SearchItemViewModel>()
            }
        }
    }
}

sealed class SearchCommand {
    data class NavigateToPlayer(val track: Track) : SearchCommand()
}

class SearchViewModel(application: Application) : AndroidViewModel(application),
    SearchAdapter.Callback {

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    val sharedPreferences = application.getSharedPreferences(PREFERENCES, MODE_PRIVATE)

    private val historyInteractor: HistoryInteractor by lazy {
        Creator.provideHistoryInteractor(
            application
        )
    }

    private val musicInteractor: MusicInteractor by lazy { Creator.provideMusicInteractor() }
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    private var latestSearchText = ""

    private val searchRunnable = Runnable { search() }

    private val stateLiveData = MutableLiveData(SearchUiState())
    fun observeState(): LiveData<SearchUiState> = stateLiveData

    private val commandLiveData = SingleLiveEvent<SearchCommand>()
    fun observeCommand(): LiveData<SearchCommand> = commandLiveData

    init {
        historyInteractor.getAllTrack(object : HistoryInteractor.HistoryConsumer {
            override fun consume(trackList: List<Track>) {
                stateLiveData.postValue(stateLiveData.value?.copy(historyTracks = trackList))
            }
        })

        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == HistoryRepositoryImpl.LAST_VIEW_KEY) {
                historyInteractor.getAllTrack(object : HistoryInteractor.HistoryConsumer {
                    override fun consume(trackList: List<Track>) {
                        stateLiveData.postValue(stateLiveData.value?.copy(historyTracks = trackList))
                    }
                })
            }
        }
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
                override fun onSuccess(findTracks: List<Track>) {
                    stateLiveData.postValue(
                        stateLiveData.value?.copy(
                            isLoading = false,
                            findTracks = findTracks,
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

    override fun onItemClick(item: SearchItemViewModel.Item) {
        clickDebounce()
        historyInteractor.addTrack(item.track)
        commandLiveData.postValue(SearchCommand.NavigateToPlayer(track = item.track))
    }

    override fun onClearHistoryClick() {
        historyInteractor.clear()
    }
}