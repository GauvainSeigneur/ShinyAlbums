package com.gauvain.seigneur.shinyalbums

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.domain.model.AlbumModel
import kotlinx.android.synthetic.main.item_user_album.view.*

class UserAlbumViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_user_album
    }

    fun bind(itemData: AlbumModel?, listener: UserAlbumListAdapter.Listener) {
        with(itemView) {
            title.text = itemData?.title?: "oops"
            userItemView.setOnClickListener {
                listener.onClick("oo")
            }
        }
    }

}