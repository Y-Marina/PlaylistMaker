package com.hfad.playlistmaker.common

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun Long.toTime(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}