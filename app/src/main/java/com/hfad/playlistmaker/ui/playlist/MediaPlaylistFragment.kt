package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.playlistmaker.databinding.FragmentMediaPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistFragment : Fragment() {
    companion object {
        fun newInstance() = MediaPlaylistFragment()
    }

    private lateinit var binding: FragmentMediaPlaylistBinding

    private val mediaPlaylistViewModel: MediaPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
}
