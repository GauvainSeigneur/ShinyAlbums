package com.gauvain.seigneur.shinyalbums.utils

import android.widget.ImageView
import coil.api.load
import coil.request.Request
import com.gauvain.seigneur.shinyalbums.R

fun ImageView.loadCover(url: String?, l: Request.Listener?=null) {
    this.load(
        url
    ) {
        placeholder(R.drawable.ic_cover_place_holder)
        error(R.drawable.ic_cover_place_holder)
        fallback(R.drawable.ic_cover_place_holder)
        l?.let {
            listener(it)
        }

    }
}

