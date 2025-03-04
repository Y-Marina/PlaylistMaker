package com.hfad.playlistmaker.ui.search

import com.hfad.playlistmaker.domian.models.Track

sealed class SearchItemViewModel {
    data object Header : SearchItemViewModel()

    data class Item(
        val track: Track
    ) : SearchItemViewModel()

    data object Button : SearchItemViewModel()
}