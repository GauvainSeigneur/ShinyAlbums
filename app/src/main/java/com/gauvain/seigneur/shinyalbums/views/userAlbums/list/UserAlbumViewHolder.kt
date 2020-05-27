package com.gauvain.seigneur.shinyalbums.views.userAlbums.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.AlbumItemData
import com.gauvain.seigneur.shinyalbums.R
import kotlinx.android.synthetic.main.item_user_album.view.*

class UserAlbumViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_user_album
    }

    fun bind(itemData: AlbumItemData?, listener: ItemClickListener) {
        with(itemView) {
            title.text = itemData?.title?: "oops"
            userAlbumItemView.setOnClickListener {
                listener.onClick(itemData?.id)
            }
        }
    }

}