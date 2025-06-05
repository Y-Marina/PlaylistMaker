package com.hfad.playlistmaker.ui.playback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.common.toTime
import com.hfad.playlistmaker.databinding.FragmentPlayBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

const val TRACK_ITEM = "track_item"

class PlayFragment : Fragment() {

    private val args by navArgs<PlayFragmentArgs>()

    private lateinit var binding: FragmentPlayBinding

    private val viewModel by viewModel<PlayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentPlayBinding.inflate(layoutInflater)

        viewModel.setTrack(args.track)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationOnClickListener { viewModel.onBackClicked() }
        }

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        binding.addToPlaylistBt.setOnClickListener {
            viewModel.onAddPlaylistClicked()
        }

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
                .transform(RoundedCorners(dpToPx(8f, requireContext())))
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
        val navController = findNavController()
        when (command) {
            is PlayCommand.AddPlaylist -> navController.navigate(R.id.addToPlaylistDialogFragment)
            is PlayCommand.NavigateBack -> navController.popBackStack()
        }
    }

    private fun setPlayTime(time: Int) {
        binding.playTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
    }
}
