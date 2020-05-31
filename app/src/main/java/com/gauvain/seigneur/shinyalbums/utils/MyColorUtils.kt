package com.gauvain.seigneur.shinyalbums.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.gauvain.seigneur.shinyalbums.R

object MyColorUtils {
    @ColorInt
    fun darkenColor(
        context: Context,
        @ColorInt
        color: Int, ratio: Float
    ): Int =
        ColorUtils.blendARGB(
            color,
            ContextCompat.getColor(context, R.color.colorBackground), ratio
        )
}

