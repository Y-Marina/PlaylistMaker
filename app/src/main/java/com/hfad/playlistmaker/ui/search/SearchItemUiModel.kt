package com.hfad.playlistmaker.ui.search

import com.hfad.playlistmaker.domian.models.Track

sealed class SearchItemUiModel {
    data object Header : SearchItemUiModel()

    data class Item(
        val track: Track
    ) : SearchItemUiModel()

    data object Button : SearchItemUiModel()
}