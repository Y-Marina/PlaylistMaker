package com.hfad.playlistmaker.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.hfad.playlistmaker.databinding.FragmentMediaFavBinding
import com.hfad.playlistmaker.ui.playback.PlayActivity
import com.hfad.playlistmaker.ui.playback.TRACK_ITEM
import com.hfad.playlistmaker.ui.search.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavFragment : Fragment() {
    companion object {
        fun newInstance() = MediaFavFragment()
    }

    private lateinit var binding: FragmentMediaFavBinding

    private val viewModel: MediaFavViewModel by viewModel()

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

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        adapter = SearchAdapter(viewModel)

        binding.contentList.adapter = adapter
    }

    private fun handleUiState(state: FavUiState) {
        binding.emptyFlow.isVisible = state.favTracks.isEmpty()
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }

    private fun handleCommand(command: FavCommand) {
        when (command) {
            is FavCommand.NavigateToPlayer -> {
                val playIntent = Intent(requireContext(), PlayActivity::class.java)
                playIntent.putExtra(TRACK_ITEM, command.track)
                startActivity(playIntent)
            }
        }
    }
}
