package com.hfad.playlistmaker.ui.playback

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.common.toTime
import com.hfad.playlistmaker.databinding.ActivityPlayBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_ITEM = "track_item"

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding

    private val viewModel by viewModel<PlayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        val trackId = intent.getLongExtra(TRACK_ITEM, -1L)
        viewModel.setTrack(trackId)

        viewModel.observeState().observe(this) { handleUiState(it) }
        viewModel.observeCommand().observe(this) { handleCommand(it) }

        binding.playBt.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.favoriteBt.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onFavoriteClicked(isChecked)
        }
    }

    private fun handleUiState(state: PlayUiState) {
        if (state.track != null) {
            Glide
                .with(this)
                .load(state.track.getCoverArtwork())
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(8f, this)))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.artworkIm)

            binding.playBt.isEnabled = state.isPlayButtonEnabled
            binding.favoriteBt.isChecked = state.track.isFavorite
            binding.trackNameTv.text = state.track.trackName
            binding.artistNameTv.text = state.track.artistName
            binding.durationTimeTv.text = state.track.trackTime.toTime()

            setPlayTime(state.currentTime)

            val collectionName = state.track.collectionName

            if (collectionName != null) {
                binding.albumNameTv.text = collectionName
            } else {
                binding.albumTv.isVisible = false
                binding.albumNameTv.isVisible = false
            }

            binding.yearNameTv.text = state.track.getYear()
            binding.genreNameTv.text = state.track.primaryGenreName
            binding.countryNameTv.text = state.track.country
        }
    }

    private fun handleCommand(command: PlayCommand) {
        when(command) {
            is PlayCommand.NavigateBack -> finish()
        }
    }

    private fun setPlayTime(time: Int) {
        binding.playTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
    }
}
