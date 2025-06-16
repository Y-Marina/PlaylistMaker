package com.hfad.playlistmaker.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.databinding.FragmentMenuBottomSheetBinding
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class MenuBottomSheet : BottomSheetDialogFragment() {
    private val args by navArgs<MenuBottomSheetArgs>()

    private lateinit var binding: FragmentMenuBottomSheetBinding

    private lateinit var adapter: PlaylistAdapter

    private val viewModel by viewModel<MenuBottomSheetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        viewModel.setPlaylist(args.playlistName)



        binding.shareTv.setOnClickListener { viewModel.onShareClick() }
    }

    private fun handleUiState(state: MenuBottomSheetUiState) {
        binding.smallPlaylistItem.playlistNameTv.text = state.name
        binding.smallPlaylistItem.trackCountTv.text = requireContext().resources
            ?.getQuantityString(
                R.plurals.track_count,
                state.trackCount,
                state.trackCount
            )

        Glide
            .with(requireContext())
            .load(state.photoUrl)
            .centerCrop()
            .transition(withCrossFade())
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.smallPlaylistItem.posterIm)
    }

    private fun handleCommand(command: MenuCommand) {
        when (command) {
            is MenuCommand.NavigateToShare -> share(command.playlistWithTracks)

            is MenuCommand.ShowToast -> Toast.makeText(
                requireContext(), R.string.empty_track_list_toast,
                Toast.LENGTH_SHORT
            ).show()

            is MenuCommand.NavigateToChangeInfo -> {}

            is MenuCommand.NavigateToDeletePlaylist -> {}
        }
    }

    private fun share(playlistWithTracks: PlaylistWithTracks) {
        val message = buildString {
            append("${playlistWithTracks.playlist.name}\n")
            append("${playlistWithTracks.playlist.description}\n")
            append("${playlistWithTracks.trackCount} треков\n")
            playlistWithTracks.tracks.forEachIndexed { index, track ->
                append("${index + 1}) ${track.artistName} - ${track.trackName} (${setPlayTime(track.trackTime)})\n")
            }
        }
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(shareIntent)
    }

    private fun setPlayTime(time: Long): String? {
        val shareTime = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
        return shareTime
    }
}