package com.hfad.playlistmaker.domian.models

import android.os.Parcelable
import com.hfad.playlistmaker.ui.common.WarningDialogExtra
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val name: String,
    val description: String?,
    val photoUrl: String? = null
) : Parcelable