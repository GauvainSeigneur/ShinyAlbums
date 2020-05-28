package com.gauvain.seigneur.shinyalbums.views.albumDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.gauvain.seigneur.presentation.model.TrackItemData

class TrackListAdapter(): ListAdapter<TrackItemData, TrackItemViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackItemViewHolder {
        return TrackItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                TrackItemViewHolder.layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrackItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<TrackItemData>() {
            override fun areItemsTheSame(
                oldItem: TrackItemData,
                newItem: TrackItemData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TrackItemData,
                newItem: TrackItemData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}