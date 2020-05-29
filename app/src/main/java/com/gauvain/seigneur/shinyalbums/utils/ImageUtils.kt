package com.gauvain.seigneur.shinyalbums.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object ImageUtils {

    /**
     * Transform image to bitmap
     * @param drawable - resource load from Glide
     * @return the bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        val width = if (!drawable.bounds.isEmpty)
            drawable.bounds.width()
        else
            drawable.intrinsicWidth
        val height = if (!drawable.bounds.isEmpty)
            drawable.bounds.height()
        else
            drawable.intrinsicHeight
        // Now we check we are > 0
        val bitmap = Bitmap.createBitmap(
            if (width <= 0) 1 else width, if (height <= 0) 1 else height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}