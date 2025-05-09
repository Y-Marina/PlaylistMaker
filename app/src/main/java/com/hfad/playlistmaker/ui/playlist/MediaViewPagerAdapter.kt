package com.hfad.playlistmaker.ui.playlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) MediaFavFragment.newInstance() else MediaPlaylistFragment.newInstance()
    }

    override fun getItemCount(): Int {
        return 2
    }
}
