package com.hfad.playlistmaker

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = SearchAdapter()

    companion object {
        const val SEARCH_TEXT_KEY = "searchTextKey"
    }

    private lateinit var updateButton: Button
    private lateinit var noInternetMessageLayout: View
    private lateinit var emptyMessageLayout: View
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView

    private var searchText = ""

    private val trackList = emptyList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateButton = findViewById(R.id.update_button)
        noInternetMessageLayout = findViewById(R.id.no_internet_message)
        emptyMessageLayout = findViewById(R.id.empty_message)
        searchEditText = findViewById(R.id.search_et)
        clearButton = findViewById(R.id.clear_im)

        val contentList = findViewById<RecyclerView>(R.id.content_list)
        val searchAdapter = SearchAdapter()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        contentList.adapter = searchAdapter
        searchAdapter.data = trackList

        adapter.data = tracks

        contentList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contentList.adapter = adapter

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        updateButton.setOnClickListener {
            search()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            clearList()
            hideAllMessage()
        }

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
                }
            }
        }

        searchEditText.addTextChangedListener(textWatcher)
    }

    private fun search() {
        if (searchEditText.text.isNotEmpty()) {
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
}