package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.FragmentMediaPlaylistBinding
import com.hfad.playlistmaker.domian.models.Playlist
import com.hfad.playlistmaker.ui.common.PlaylistDecoration
import com.hfad.playlistmaker.ui.common.getParcelableCompatWrapper
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistFragment : Fragment() {
    companion object {
        fun newInstance() = MediaPlaylistFragment()
        val createPlaylistKey = "${MediaPlaylistFragment::class.qualifiedName}.createPlaylistKey"
    }

    private lateinit var binding: FragmentMediaPlaylistBinding

    private val viewModel: MediaPlaylistViewModel by viewModel()

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

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                MediaPlaylistFragmentDirections.actionToCreatePlaylistFragment(
                    createPlaylistKey
                )
            )
        }

        adapter = PlaylistAdapter(viewModel)
        binding.contentList.adapter = adapter
        binding.contentList.layoutManager = GridLayoutManager(requireContext(), 2,
            GridLayoutManager.VERTICAL, false)
        binding.contentList.addItemDecoration(PlaylistDecoration())

//        adapter.data =
//        adapter.notifyDataSetChanged()
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
