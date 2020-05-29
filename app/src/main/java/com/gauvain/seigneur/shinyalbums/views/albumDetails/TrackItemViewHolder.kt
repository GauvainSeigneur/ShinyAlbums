package com.gauvain.seigneur.shinyalbums.views.albumDetails

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.TrackItemData
import com.gauvain.seigneur.shinyalbums.R
import kotlinx.android.synthetic.main.item_track.view.*

class TrackItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_track
    }

    fun bind(itemData: TrackItemData) {
        with(itemView) {
            trackTitleView.text = itemData.title
            trackArtistTextView.text = itemData.artistName
            if (itemData.isExplicitLyrics) {
                trackTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_explicit_title_icon,
                    0,
                    0,
                    0
                )
            } else {
                trackTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
            }
        }
    }
}