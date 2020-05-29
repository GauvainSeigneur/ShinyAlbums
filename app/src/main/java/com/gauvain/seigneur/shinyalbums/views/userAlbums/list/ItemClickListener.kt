package com.gauvain.seigneur.shinyalbums.views.userAlbums.list

import android.view.View

interface ItemClickListener {
    fun onClick(id: Long?,
                rootView: View,
                cardView: View,
                imageView: View)
}