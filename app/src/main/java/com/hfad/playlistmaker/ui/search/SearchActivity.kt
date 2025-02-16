package com.hfad.playlistmaker.ui.search

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.playlistmaker.Creator
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.data.SearchHistory
import com.hfad.playlistmaker.domian.api.MusicInteractor
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.settings.PREFERENCES
import com.hfad.playlistmaker.ui.playback.PlayActivity
import com.hfad.playlistmaker.ui.playback.TRACK_ITEM

class SearchActivity : AppCompatActivity(), SearchAdapter.Callback {

    private val tracks = ArrayList<Track>()
    private val adapter = SearchAdapter(this)

    private val lastViewTracks = ArrayList<Track>()
    private val lastViewAdapter = SearchAdapter(this)

    private lateinit var searchHistory: SearchHistory

    companion object {
        const val SEARCH_TEXT_KEY = "searchTextKey"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val searchRunnable = Runnable { search() }

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    private lateinit var progressBar: ProgressBar
    private lateinit var clearListButton: Button
    private lateinit var updateButton: Button
    private lateinit var noInternetMessageLayout: View
    private lateinit var emptyMessageLayout: View
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var historyView: View
    private lateinit var historyList: RecyclerView

    private var searchText = ""

    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private lateinit var musicInteractor: MusicInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressBar = findViewById(R.id.progress_bar)
        clearListButton = findViewById(R.id.clear_list_button)
        updateButton = findViewById(R.id.update_button)
        noInternetMessageLayout = findViewById(R.id.no_internet_message)
        emptyMessageLayout = findViewById(R.id.empty_message)
        searchEditText = findViewById(R.id.search_et)
        clearButton = findViewById(R.id.clear_im)
        historyView = findViewById(R.id.history_view)
        historyList = findViewById(R.id.history_list)

        musicInteractor = Creator.provideMusicInteractor()

        val contentList = findViewById<RecyclerView>(R.id.content_list)

        val sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        searchHistory = SearchHistory(sharedPreferences)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        contentList.adapter = adapter
        adapter.data = tracks

        historyList.adapter = lastViewAdapter
        lastViewAdapter.data = lastViewTracks

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        clearListButton.setOnClickListener {
            searchHistory.clear()
            hideHistoryList()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            clearList()
            hideAllMessage()
        }

        lastViewTracks.addAll(searchHistory.getAllTrack())
        showHistoryList()

        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == SearchHistory.LAST_VIEW_KEY) {
                lastViewTracks.clear()
                lastViewTracks.addAll(searchHistory.getAllTrack())
                lastViewAdapter.notifyDataSetChanged()
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.isVisible = !p0.isNullOrEmpty()
                searchDebounce()
            }

            override fun afterTextChanged(text: Editable?) {
                searchText = text.toString()

                if (text?.isEmpty() == true) {
                    clearList()
                    hideAllMessage()
                    showHistoryList()
                }
            }
        }

        searchEditText.addTextChangedListener(textWatcher)
    }

    private fun showHistoryList() {
        if (lastViewTracks.isNotEmpty()) {
            historyView.visibility = View.VISIBLE
        }
    }

    private fun hideHistoryList() {
        historyView.visibility = View.GONE
    }

    private fun search() {
        if (searchEditText.text.isNotEmpty()) {

            progressBar.visibility = View.VISIBLE

            hideHistoryList()

            musicInteractor.searchTracks(searchText, object : MusicInteractor.TracksConsumer {
                override fun onSuccess(foundTracks: List<Track>) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        tracks.clear()
                        if (foundTracks.isNotEmpty()) {
                            tracks.addAll(foundTracks)
                            adapter.notifyDataSetChanged()
                        }

                        if (tracks.isEmpty()) {
                            showEmptyMessage()
                        } else {
                            hideAllMessage()
                        }
                    }
                }

                override fun onFailure(exception: Exception) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        showNoInternetMessage()
                    }
                }
            })
        }
    }

    private fun clearList() {
        tracks.clear()
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyMessage() {
        noInternetMessageLayout.visibility = View.GONE
        emptyMessageLayout.visibility = View.VISIBLE
        clearList()
    }

    private fun hideAllMessage() {
        emptyMessageLayout.visibility = View.GONE
        noInternetMessageLayout.visibility = View.GONE
    }

    private fun showNoInternetMessage() {
        emptyMessageLayout.visibility = View.GONE
        noInternetMessageLayout.visibility = View.VISIBLE
        clearList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TEXT_KEY, searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val text = savedInstanceState.getString(SEARCH_TEXT_KEY, "")
        searchEditText.setText(text)
    }

    override fun onItemClick(track: Track) {
        clickDebounce()
        searchHistory.addTrack(track)
        val playIntent = Intent(this, PlayActivity::class.java)
        playIntent.putExtra(TRACK_ITEM, track)
        startActivity(playIntent)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}