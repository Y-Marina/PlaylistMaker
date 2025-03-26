package com.hfad.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.ActivitySearchBinding
import com.hfad.playlistmaker.ui.playback.PlayActivity
import com.hfad.playlistmaker.ui.playback.TRACK_ITEM
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeState().observe(this) { handleUiState(it) }
        viewModel.observeCommand().observe(this) { handleCommand(it) }

        adapter = SearchAdapter(viewModel)
        binding.contentList.adapter = adapter

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search()
                true
            }
            false
        }

        binding.clearIm.setOnClickListener {
            binding.searchEt.setText("")
            viewModel.onClearClicked()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clearIm.isVisible = !p0.isNullOrEmpty()
                viewModel.searchDebounce(p0.toString())
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.searchDebounce(text.toString())
            }
        }

        binding.searchEt.addTextChangedListener(textWatcher)
    }

    private fun handleUiState(state: SearchUiState) {
        println("MyTag $state items = ${state.items}")
        binding.progressBar.isVisible = state.isLoading
        binding.emptyMessage.root.isVisible = state.isEmptyResult
        binding.noInternetMessage.root.isVisible = state.isError
        binding.contentList.isVisible = state.isContentVisible
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }

    private fun handleCommand(command: SearchCommand) {
        when (command) {
            is SearchCommand.NavigateToPlayer -> {
                val playIntent = Intent(this, PlayActivity::class.java)
                playIntent.putExtra(TRACK_ITEM, command.trackId)
                startActivity(playIntent)
            }
        }
    }
}
