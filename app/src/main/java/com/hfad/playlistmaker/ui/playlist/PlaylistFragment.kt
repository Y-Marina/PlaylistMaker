package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.FragmentPlaylistBinding
import com.hfad.playlistmaker.domian.models.Track
import com.hfad.playlistmaker.ui.common.DialogResult
import com.hfad.playlistmaker.ui.common.WarningDialogResult
import com.hfad.playlistmaker.ui.playback.PlayFragmentDirections
import com.hfad.playlistmaker.ui.search.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        adapter = SearchAdapter(viewModel)

        binding.contentList.adapter = adapter
    }

    private fun handleUiState(state: GetPlaylistUiState) {
        if (state.playlistWithTracks != null) {
            Glide
                .with(this)
                .load(state.playlistWithTracks)
                .centerCrop()
                .transition(withCrossFade())
//                .transform(RoundedCorners(dpToPx(8f, requireContext())))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.playlistPosterIm)

            binding.playlistNameTv.text = state.playlistWithTracks.playlist.name
            binding.playlistDescriptionTv.text = state.playlistWithTracks.playlist.description

            binding.playlistTimeTv.text = context?.resources
                ?.getQuantityString(
                    R.plurals.track_time,
                    state.playlistWithTracks.time,
                    state.playlistWithTracks.time
                )
            binding.playlistTrackCountTv.text = context?.resources
                ?.getQuantityString(
                    R.plurals.track_count,
                    state.playlistWithTracks.trackCount,
                    state.playlistWithTracks.trackCount
                )

            adapter.data = state.trackItem
            adapter.notifyDataSetChanged()
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
                    positiveButton = getString(R.string.dialog_positive_button_delete_track),
                    neutralButton = getString(R.string.dialog_neutral_button_delete_track),
                    extra = command.track
                )
            )
        }
    }
}
