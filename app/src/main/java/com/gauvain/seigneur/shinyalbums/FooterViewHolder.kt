package com.gauvain.seigneur.shinyalbums

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.domain.model.AlbumModel

class FooterViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_footer
    }

    fun bind(itemData: AlbumModel?, listener: UserAlbumListAdapter.Listener) {
        with(itemView) {

        }
    }

}