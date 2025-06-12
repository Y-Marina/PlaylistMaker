package com.hfad.playlistmaker.ui.common

import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.getParcelableCompatWrapper(key: String): T? {
    @Suppress("DEPRECATION")
    return getParcelable(key)
}