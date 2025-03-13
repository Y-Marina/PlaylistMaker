package com.hfad.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.ActivityMainBinding
import com.hfad.playlistmaker.ui.playlist.MediaActivity
import com.hfad.playlistmaker.ui.search.SearchActivity
import com.hfad.playlistmaker.ui.settings.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeCommand().observe(this) { handleCommand(it) }

        binding.searchCard.setOnClickListener {
            viewModel.onSearchClick()
        }

        binding.mediaCard.setOnClickListener {
            viewModel.onMediaClick()
        }

        binding.settingsCard.setOnClickListener {
            viewModel.onSettingsClick()
        }
    }

    private fun handleCommand(command: MainCommand) {
        when(command) {
            is MainCommand.NavigationToSearch -> {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivity(searchIntent)
            }
            is MainCommand.NavigationToMedia -> {
                val mediaIntent = Intent(this, MediaActivity::class.java)
                startActivity(mediaIntent)
            }
            is MainCommand.NavigationToSettings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }
    }
}