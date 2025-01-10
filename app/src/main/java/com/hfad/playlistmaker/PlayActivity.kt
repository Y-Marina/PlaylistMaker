package com.hfad.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.playlistmaker.common.dpToPx

const val TRACK_ITEM = "track_item"

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        val track = intent.getParcelableExtra<Track>(TRACK_ITEM)

        if (track == null) {
            finish()
        } else {
            val imageView = findViewById<ImageView>(R.id.artwork_im)
            val trackName = findViewById<TextView>(R.id.track_name_tv)
            val artistName = findViewById<TextView>(R.id.artist_name_tv)
            val progressTime = findViewById<TextView>(R.id.play_time)
            val durationTime = findViewById<TextView>(R.id.duration_time_tv)
            val album = findViewById<TextView>(R.id.album_tv)
            val albumName = findViewById<TextView>(R.id.album_name_tv)
            val year = findViewById<TextView>(R.id.year_name_tv)
            val genre = findViewById<TextView>(R.id.genre_name_tv)
            val country = findViewById<TextView>(R.id.country_name_tv)

            Glide
                .with(this)
                .load(track.getCoverArtwork())
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(8f, this)))
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView)

            trackName.text = track.trackName
            artistName.text = track.artistName
            progressTime.text = track.getTime()
            durationTime.text = track.getTime()

            val collectionName = track.collectionName

            if (collectionName != null) {
                albumName.text = collectionName
            } else {
                album.isVisible = false
                albumName.isVisible = false
            }

            year.text = track.getYear()
            genre.text = track.primaryGenreName
            country.text = track.country

            println("myTag ${track.trackName}")
        }
    }
}