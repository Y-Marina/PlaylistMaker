package com.hfad.playlistmaker.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaylistDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.setEmpty()

        outRect.set(0, 8.dp, 0, 8.dp)

        val viewHolder = parent.getChildViewHolder(view)

        val lp = (viewHolder.itemView.layoutParams as GridLayoutManager.LayoutParams)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount

        when (lp.spanIndex) {
            0 -> {
                outRect.left = 16.dp
                outRect.right = 4.dp
            }

            spanCount - 1 -> {
                outRect.left = 4.dp
                outRect.right = 16.dp
            }

            else -> {
                outRect.left = 4.dp
                outRect.right = 4.dp
            }
        }
    }
}