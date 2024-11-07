package com.hfad.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
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

         searchCard.setOnClickListener{
             val searchIntent = Intent(this, SearchActivity::class.java)
             startActivity(searchIntent)
         }

        mediaCard.setOnClickListener {
            val mediaIntent = Intent(this,MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsCard.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}