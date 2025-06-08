package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.hfad.playlistmaker.databinding.FragmentAddToPlaylistDialogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddToPlaylistDialogFragment : BottomSheetDialogFragment() {
    private val args by navArgs<AddToPlaylistDialogFragmentArgs>()

    private lateinit var binding: FragmentAddToPlaylistDialogBinding

    private val viewModel by viewModel<AddToPlaylistDialogViewModel>()

    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddToPlaylistDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylistState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        viewModel.setTrack(args.track)

        setFragmentResultListener(MediaPlaylistFragment.Companion.createPlaylistKey) { _, bundle ->
            val playlistName = CreatePlaylistResult.fromBundle(bundle).playlistName
            val message = "Плейлист $playlistName создан"
            val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            val snackbarLayout = snackbar.view
            val snackbarText = snackbarLayout
                .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.textAlignment = TEXT_ALIGNMENT_CENTER
            snackbar.show()
        }

        adapter = PlaylistAdapter(viewModel)
        binding.contentList.adapter = adapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                AddToPlaylistDialogFragmentDirections.actionToCreatePlaylistFragment(
                    args.resultKey,
                    args.track
                )
            )
        }
    }

    private fun handleUiState(state: AddPlaylistUiState) {
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }

    private fun handleCommand(command: DialogCommand) {
        when (command) {
            is DialogCommand.Success -> {
                setFragmentResult(args.resultKey, AddPlaylistResult(true, command.name).toBundle())
                findNavController().popBackStack()
            }

            is DialogCommand.Error -> {
                setFragmentResult(args.resultKey, AddPlaylistResult(false, command.name).toBundle())
                findNavController().popBackStack()
            }
        }
    }
}