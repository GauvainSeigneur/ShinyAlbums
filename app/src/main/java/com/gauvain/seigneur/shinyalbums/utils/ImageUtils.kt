package com.gauvain.seigneur.shinyalbums.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.palette.graphics.Palette

/**
 * Transform image to bitmap
 * @param drawable - resource load from Glide
 * @return the bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return this.bitmap
    }
    // We ask for the bounds if they have been set as they would be most
    // correct, then we check we are  > 0
    val width = if (!this.bounds.isEmpty)
        this.bounds.width()
    else
        this.intrinsicWidth
    val height = if (!this.bounds.isEmpty)
        this.bounds.height()
    else
        this.intrinsicHeight
    // Now we check we are > 0
    val bitmap = Bitmap.createBitmap(
        if (width <= 0) 1 else width, if (height <= 0) 1 else height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}

fun Drawable.getDominantSwatch(): Palette.Swatch? {
    val p = Palette.from(this.toBitmap()).generate()
    return p.dominantSwatch
}
