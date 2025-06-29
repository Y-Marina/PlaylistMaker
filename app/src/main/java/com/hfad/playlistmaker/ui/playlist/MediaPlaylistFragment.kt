package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hfad.playlistmaker.databinding.FragmentMediaPlaylistBinding
import com.hfad.playlistmaker.ui.common.PlaylistDecoration

class MediaPlaylistFragment : Fragment() {
    companion object {
        fun newInstance() = MediaPlaylistFragment()
        val createPlaylistKey = "${MediaPlaylistFragment::class.qualifiedName}.createPlaylistKey"
    }

    private lateinit var binding: FragmentMediaPlaylistBinding

    private val viewModel: MediaViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylistState().observe(viewLifecycleOwner) { handleUiState(it) }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                MediaPlaylistFragmentDirections.actionToCreatePlaylistFragment(
                    createPlaylistKey,
                    null
                )
            )
        }

        adapter = PlaylistAdapter(viewModel)
        binding.contentList.adapter = adapter
        binding.contentList.layoutManager = GridLayoutManager(
            requireContext(), 2,
            GridLayoutManager.VERTICAL, false
        )

        binding.contentList.addItemDecoration(PlaylistDecoration())
        binding.contentList.doOnNextLayout {
            binding.contentList.invalidateItemDecorations()
        }
    }

    private fun handleUiState(state: PlaylistUiState) {
        binding.emptyFlow.isVisible = state.playlists.isEmpty()
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }
}
