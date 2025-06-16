package com.hfad.playlistmaker.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.databinding.FragmentPlaylistBinding
import com.hfad.playlistmaker.domian.models.PlaylistWithTracks
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.common.DialogResult
import com.hfad.playlistmaker.ui.common.WarningDialogResult
import com.hfad.playlistmaker.ui.playback.PlayFragmentDirections
import com.hfad.playlistmaker.ui.search.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {
    companion object {
        private val className = PlaylistFragment::class.qualifiedName
        private val deleteDialogKey = "${className}.deleteDialogKey"
    }

    private lateinit var binding: FragmentPlaylistBinding

    private val viewModel: PlaylistViewModel by viewModel()

    private val args by navArgs<PlaylistFragmentArgs>()

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentPlaylistBinding.inflate(layoutInflater)

        viewModel.setPlaylistName(args.playlistName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(deleteDialogKey) { _, bundle ->
            val warningDialogResult = WarningDialogResult.fromBundle(bundle)
            if (warningDialogResult.result == DialogResult.Ok && warningDialogResult.extra is Track) {
                viewModel.deleteTrackFromPlaylist(warningDialogResult.extra)
            }
        }

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        binding.toolbar.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationOnClickListener { viewModel.onBackClicked() }
        }

        binding.standardBottomSheet.setBackgroundResource(R.drawable.behavior_corners)

        adapter = SearchAdapter(viewModel)

        binding.contentList.adapter = adapter
    }

    private fun handleUiState(state: GetPlaylistUiState) {
        if (state.playlistWithTracks != null) {
            Glide
                .with(requireContext())
                .load(state.playlistWithTracks.playlist.photoUrl)
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(4f, requireContext())))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.playlistPosterIm)

            binding.playlistNameTv.text = state.playlistWithTracks.playlist.name
            binding.playlistDescriptionTv.text = state.playlistWithTracks.playlist.description

            binding.playlistTimeTv.text = requireContext().resources
                ?.getQuantityString(
                    R.plurals.track_time,
                    state.playlistWithTracks.time,
                    state.playlistWithTracks.time
                )
            binding.playlistTrackCountTv.text = requireContext().resources
                ?.getQuantityString(
                    R.plurals.track_count,
                    state.playlistWithTracks.trackCount,
                    state.playlistWithTracks.trackCount
                )

            binding.emptyTrack.isVisible = state.trackItem.isEmpty()
            binding.contentList.isVisible = state.trackItem.isNotEmpty()

            adapter.data = state.trackItem
            adapter.notifyDataSetChanged()

            binding.shareIm.setOnClickListener { viewModel.onShareClick() }
            binding.menuIm.setOnClickListener { viewModel.onMenuClick() }
        }
    }

    private fun handleCommand(command: PlaylistCommand) {
        val navController = findNavController()
        when (command) {
            PlaylistCommand.NavigateBack -> navController.popBackStack()

            is PlaylistCommand.NavigateToPlayer -> navController.navigate(
                PlayFragmentDirections.toPlayFragment(command.track)
            )

            is PlaylistCommand.ShowDeleteTrackDialog -> navController.navigate(
                PlaylistFragmentDirections.toWarningDialog(
                    resultKey = deleteDialogKey,
                    title = getString(R.string.dialog_message_delete_track),
                    message = "",
                    positiveButton = getString(R.string.dialog_positive_button_delete),
                    neutralButton = getString(R.string.dialog_neutral_button_delete),
                    extra = command.track
                )
            )

            is PlaylistCommand.NavigateToShare -> share(command.playlistWithTracks)

            is PlaylistCommand.ShowToast -> Toast.makeText(
                requireContext(), R.string.empty_track_list_toast,
                Toast.LENGTH_SHORT
            ).show()

            is PlaylistCommand.NavigateToMenu -> navController.navigate(
                PlaylistFragmentDirections.actionToMenuBottomSheet(command.playlistName)
            )

            is PlaylistCommand.NavigateToMediaFragment -> navController.navigate(
                PlaylistFragmentDirections.toMediaFragment()
            )
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
