package com.gauvain.seigneur.shinyalbums.views.userAlbums.list

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.gauvain.seigneur.presentation.model.AlbumItemData
import com.gauvain.seigneur.shinyalbums.R
import com.gauvain.seigneur.shinyalbums.utils.loadCover
import kotlinx.android.synthetic.main.item_user_album.view.*

class UserAlbumViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_user_album
    }

    fun bind(itemData: AlbumItemData?, listener: ItemClickListener) {
        with(itemView) {
            coverImageView.loadCover(itemData?.cover)
            titleTextView.text = itemData?.title?: "oops"
            artistTextView.text =  itemData?.artistName
            userAlbumItemView.setOnClickListener {
                Log.d("itemCmlick", "id ${itemData?.albumTrackId}")
                listener.onClick(itemData?.id, itemBackground, coverCardView, coverImageView)
            }
        }
    }

}