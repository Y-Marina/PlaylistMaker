package com.hfad.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hfad.playlistmaker.databinding.FragmentMediaFavBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavFragment : Fragment() {
    private lateinit var binding: FragmentMediaFavBinding

    private val mediaFavViewModel: MediaFavViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaFavBinding.inflate(inflater, container, false)
        return binding.root
    }
}
