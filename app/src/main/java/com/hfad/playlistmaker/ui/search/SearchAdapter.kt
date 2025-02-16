package com.hfad.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.domian.models.Track

class SearchAdapter(val callback: Callback) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    interface Callback {
        fun onItemClick(track: Track)
    }

    var data: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class SearchViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val trackName = item.findViewById<TextView>(R.id.track_name_tv)
        private val artistName = item.findViewById<TextView>(R.id.artist_name_tv)
        private val trackTime = item.findViewById<TextView>(R.id.track_time_tv)
        private val artwork = item.findViewById<ImageView>(R.id.artwork_im)
        private val context = item.rootView.context

        init {
            item.rootView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    callback.onItemClick(data[position])
                }
            }
        }

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.getTime()
            Glide
                .with(context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(2f, context)))
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }
    }
}