package com.hfad.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), SearchAdapter.Callback {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = SearchAdapter(this)

    private val lastViewTracks = ArrayList<Track>()
    private val lastViewAdapter = SearchAdapter(this)

    private lateinit var searchHistory: SearchHistory

    companion object {
        const val SEARCH_TEXT_KEY = "searchTextKey"
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        clearListButton = findViewById(R.id.clear_list_button)
        updateButton = findViewById(R.id.update_button)
        noInternetMessageLayout = findViewById(R.id.no_internet_message)
        emptyMessageLayout = findViewById(R.id.empty_message)
        searchEditText = findViewById(R.id.search_et)
        clearButton = findViewById(R.id.clear_im)
        historyView = findViewById(R.id.history_view)
        historyList = findViewById(R.id.history_list)

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

        updateButton.setOnClickListener {
            search()
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
            hideHistoryList()
            iTunesService.search(searchEditText.text.toString())
                .enqueue(object : Callback<MusicResponse> {
                    override fun onResponse(
                        call: Call<MusicResponse>,
                        response: Response<MusicResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                showEmptyMessage()
                            } else {
                                hideAllMessage()
                            }
                        } else {
                            showNoInternetMessage()
                        }
                    }

                    override fun onFailure(
                        call: Call<MusicResponse>,
                        throwable: Throwable
                    ) {
                        showNoInternetMessage()
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
        searchHistory.addTrack(track)
        val playIntent = Intent(this, PlayActivity::class.java)
        playIntent.putExtra(TRACK_ITEM, track)
        startActivity(playIntent)
    }
}