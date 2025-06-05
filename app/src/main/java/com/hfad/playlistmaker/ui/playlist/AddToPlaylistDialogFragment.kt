package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hfad.playlistmaker.databinding.FragmentAddToPlaylistDialogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddToPlaylistDialogFragment : BottomSheetDialogFragment() {
    companion object {
        val createPlaylistKey = "${AddToPlaylistDialogFragment::class.qualifiedName}.createPlaylistKey"
    }

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

        adapter = PlaylistAdapter(viewModel)
        binding.contentList.adapter = adapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                AddToPlaylistDialogFragmentDirections.actionToCreatePlaylistFragment(
                    createPlaylistKey
                )
            )
        }
    }

    private fun handleUiState(state: AddPlaylistUiState) {
        println("myTag $state")
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }
}