package com.gauvain.seigneur.shinyalbums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gauvain.seigneur.domain.model.AlbumModel

class UserAlbumListAdapter(
    private val listener: Listener
) : PagedListAdapter<AlbumModel, UserAlbumViewHolder>
    (DiffCallback) {

    interface Listener {
        fun onClick(
            id: String
        )
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<AlbumModel>() {
            override fun areItemsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAlbumViewHolder =
        UserAlbumViewHolder(
            LayoutInflater.from(parent.context).inflate(
                UserAlbumViewHolder.layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: UserAlbumViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}
