package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.FragmentMediaBinding
import com.hfad.playlistmaker.ui.playlist.MediaPlaylistFragment.Companion.createPlaylistKey
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment : Fragment() {

    private val viewModel: MediaViewModel by viewModel()

    private lateinit var binding: FragmentMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(createPlaylistKey) { _, bundle ->
            val playlistName = CreatePlaylistResult.fromBundle(bundle).playlistName
            val message = "Плейлист $playlistName создан"
            val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            val snackbarLayout = snackbar.view
            val snackbarText = snackbarLayout
                .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snackbarText.textAlignment = TEXT_ALIGNMENT_CENTER
            snackbar.show()
        }

        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.fav_tracks)
                else -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    private fun handleCommand(command: MediaCommand) {
        val navController = findNavController()
        when (command) {
            is MediaCommand.NavigateToNewPlaylist -> {}
            is MediaCommand.NavigateToPlayer -> navController.navigate(
                MediaFragmentDirections.toPlayFragment(command.track)
            )

            is MediaCommand.NavigateToPlaylist -> navController.navigate(
                MediaFragmentDirections.toPlaylistFragment(command.playlistName)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}
