package com.hfad.playlistmaker.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.common.dpToPx
import com.hfad.playlistmaker.databinding.PlaylistBigItemBinding
import com.hfad.playlistmaker.databinding.PlaylistSmallItemBinding

class PlaylistAdapter(val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface Callback {
        fun onItemClick(item: PlaylistItemUiModel)
    }

    var data: List<PlaylistItemUiModel> = emptyList()

    override fun getItemViewType(position: Int): Int {
        val item = data[position]

        return when(item) {
            is PlaylistItemUiModel.BigPlaylistItem -> R.layout.playlist_big_item
            is PlaylistItemUiModel.SmallPlaylistItem -> R.layout.playlist_small_item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.playlist_big_item -> {
                PlaylistBigViewHolder(PlaylistBigItemBinding.inflate(inflater, parent, false))
            }

            R.layout.playlist_small_item -> {
                PlaylistSmallViewHolder(PlaylistSmallItemBinding.inflate(inflater, parent, false))
            }

            else -> throw IllegalArgumentException("Неожиданный viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(val item = data[position]) {
            is PlaylistItemUiModel.BigPlaylistItem -> (holder as PlaylistBigViewHolder).bind(item)
            is PlaylistItemUiModel.SmallPlaylistItem -> (holder as PlaylistSmallViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PlaylistBigViewHolder(val binding: PlaylistBigItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = data[position]
                    callback.onItemClick(item)
                }
            }
        }

        fun bind(item: PlaylistItemUiModel.BigPlaylistItem) = with(binding) {
            playlistNameTv.text = item.name
            trackCountTv.text = context.resources
                .getQuantityString(R.plurals.track_count, item.trackCount, item.trackCount)
            Glide
                .with(context)
                .load(item.photoUrl?.toUri())
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(2f, context)))
                .placeholder(R.drawable.ic_placeholder)
                .into(playlistPosterIm)
        }
    }

    inner class PlaylistSmallViewHolder(val binding: PlaylistSmallItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = data[position]
                    callback.onItemClick(item)
                }
            }
        }

        fun bind(item: PlaylistItemUiModel.SmallPlaylistItem) = with(binding) {
            playlistNameTv.text = item.name
            trackCountTv.text = context.resources
                .getQuantityString(R.plurals.track_count, item.trackCount, item.trackCount)
            Glide
                .with(context)
                .load(item.photoUrl)
                .centerCrop()
                .transition(withCrossFade())
                .transform(RoundedCorners(dpToPx(4f, context)))
                .placeholder(R.drawable.ic_placeholder)
                .into(posterIm)
        }
    }
}