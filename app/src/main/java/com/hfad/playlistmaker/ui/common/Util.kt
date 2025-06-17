package com.hfad.playlistmaker.ui.common

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.widget.TextView

inline fun <reified T : Parcelable> Bundle.getParcelableCompatWrapper(key: String): T? {
    @Suppress("DEPRECATION")
    return getParcelable(key)
}

fun TextView.setTextIfDiffer(text: CharSequence?) {
    if (!TextUtils.equals(this.text, text)) {
        setText(text)
    }
}