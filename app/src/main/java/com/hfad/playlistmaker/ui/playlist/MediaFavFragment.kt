package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hfad.playlistmaker.databinding.FragmentMediaFavBinding
import com.hfad.playlistmaker.ui.search.SearchAdapter

class MediaFavFragment : Fragment() {
    companion object {
        fun newInstance() = MediaFavFragment()
    }

    private lateinit var binding: FragmentMediaFavBinding

    private val viewModel: MediaViewModel by viewModels({ requireParentFragment() })

    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFavState().observe(viewLifecycleOwner) { handleUiState(it) }

        adapter = SearchAdapter(viewModel)

        binding.contentList.adapter = adapter
    }

    private fun handleUiState(state: FavUiState) {
        binding.emptyFlow.isVisible = state.favTracks.isEmpty()
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }
}
