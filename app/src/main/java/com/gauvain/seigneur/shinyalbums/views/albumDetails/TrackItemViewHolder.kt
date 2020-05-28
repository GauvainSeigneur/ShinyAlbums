package com.gauvain.seigneur.shinyalbums.views.albumDetails

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.TrackItemData
import com.gauvain.seigneur.shinyalbums.R
import kotlinx.android.synthetic.main.item_user_album.view.*

class TrackItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_user_album
    }

    fun bind(itemData: TrackItemData) {
        with(itemView) {
            titleTextView.text = itemData.title
        }
    }

}