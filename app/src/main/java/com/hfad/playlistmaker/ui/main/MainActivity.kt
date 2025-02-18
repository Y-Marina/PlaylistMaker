package com.hfad.playlistmaker.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.hfad.playlistmaker.Creator
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.ui.playlist.MediaActivity
import com.hfad.playlistmaker.ui.search.SearchActivity
import com.hfad.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchCard = findViewById<MaterialCardView>(R.id.search_card)
        val mediaCard = findViewById<MaterialCardView>(R.id.media_card)
        val settingsCard = findViewById<MaterialCardView>(R.id.settings_card)

        if (settingsInteractor.hasSavedTheme()) {
            val isNightMode = (resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            settingsInteractor.saveTheme(isNightMode)
        } else {
            val theme = settingsInteractor.getTheme()
            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchCard.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        mediaCard.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsCard.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}