package com.hfad.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
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
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_ITEM = "track_item"

class PlayActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIMER_DEBOUNCE = 1000L
    }

    private var playerState = STATE_DEFAULT

    private val mediaPlayer = MediaPlayer()

    private val playTimeRunnable = object : Runnable {
        var currentTime = 0
        override fun run() {
            val t = mediaPlayer.currentPosition
            currentTime = t.coerceAtLeast(currentTime)
            println("myTag t = $t, c = $currentTime")
            setPlayTime(currentTime)
            handler.postDelayed(this, TIMER_DEBOUNCE)
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var playButton: Button
    private lateinit var progressTime: TextView

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

        playButton = findViewById(R.id.play_bt)
        progressTime = findViewById(R.id.play_time)

        val track = intent.getParcelableExtra<Track>(TRACK_ITEM)

        if (track == null) {
            finish()
        } else {
            val imageView = findViewById<ImageView>(R.id.artwork_im)
            val trackName = findViewById<TextView>(R.id.track_name_tv)
            val artistName = findViewById<TextView>(R.id.artist_name_tv)
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
            durationTime.text = track.getTime()

            setPlayTime(0)

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
        }

        track?.let { preparePlayer(track) }

        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(playTimeRunnable)
            setPlayTime(0)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        handler.postDelayed(playTimeRunnable, TIMER_DEBOUNCE)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        handler.removeCallbacks(playTimeRunnable)
    }

    private fun setPlayTime(time: Int) {
        progressTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
    }
}