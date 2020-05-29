package com.gauvain.seigneur.shinyalbums.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.gauvain.seigneur.shinyalbums.R

fun ImageView.loadCover(
    context: Context,
    url: String?,
    listener: RequestListener<Drawable>? = null
) {
    Glide.with(context)
        .load(url)
        .listener(listener)
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_cover_place_holder)
                .error(R.drawable.ic_cover_place_holder)
                .fallback(R.drawable.ic_cover_place_holder) //in case of null value
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}


