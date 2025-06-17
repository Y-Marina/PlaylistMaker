package com.hfad.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.common.toTime
import com.hfad.playlistmaker.databinding.TrackListButtonBinding
import com.hfad.playlistmaker.databinding.TrackListHeadBinding
import com.hfad.playlistmaker.databinding.TrackListItemBinding

class SearchAdapter(val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface Callback {
        fun onItemClick(item: SearchItemUiModel.Item)
        fun onItemLongClick(item: SearchItemUiModel.Item) {}
        fun onClearHistoryClick() {}
    }

    var data: List<SearchItemUiModel> = emptyList()

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return when (item) {
            is SearchItemUiModel.Header -> R.layout.track_list_head
            is SearchItemUiModel.Item -> R.layout.track_list_item
            is SearchItemUiModel.Button -> R.layout.track_list_button
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.track_list_head -> {
                TrackHeaderViewHolder(TrackListHeadBinding.inflate(inflater, parent, false))
            }

            R.layout.track_list_item -> {
                TrackItemViewHolder(TrackListItemBinding.inflate(inflater, parent, false))
            }

            R.layout.track_list_button -> {
                TrackButtonViewHolder(TrackListButtonBinding.inflate(inflater, parent, false))
            }

            else -> throw IllegalArgumentException("Неожиданный viewType")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = data[position]) {
            is SearchItemUiModel.Header -> Unit
            is SearchItemUiModel.Item -> (holder as TrackItemViewHolder).bind(item)
            is SearchItemUiModel.Button -> Unit
        }
    }

    inner class TrackHeaderViewHolder(binding: TrackListHeadBinding) : RecyclerView.ViewHolder(binding.root)

    inner class TrackItemViewHolder(val binding: TrackListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = data[position] as? SearchItemUiModel.Item
                    if (item != null) {
                        callback.onItemClick(item)
                    }
                }
            }

            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = data[position] as? SearchItemUiModel.Item
                    if (item != null) {
                        callback.onItemLongClick(item)
                        return@setOnLongClickListener true
                    } else {
                        return@setOnLongClickListener false
                    }
                } else {
                    return@setOnLongClickListener false
                }
            }
        }

        fun bind(item: SearchItemUiModel.Item) = with(binding) {
            trackNameTv.text = item.track.trackName
            artistNameTv.text = item.track.artistName
            trackTimeTv.text = item.track.trackTime.toTime()
            Glide
                .with(context)
                .load(item.track.artworkUrl100)
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(2f, context)))
                .placeholder(R.drawable.ic_placeholder)
                .into(artworkIm)
        }
    }

    inner class TrackButtonViewHolder(binding: TrackListButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clearListButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    callback.onClearHistoryClick()
                }
            }
        }
    }
}