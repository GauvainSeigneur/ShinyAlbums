package com.gauvain.seigneur.shinyalbums.utils


import android.graphics.Color
import androidx.core.graphics.ColorUtils

object MyColorUtils {

    @JvmStatic
    fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.75
    }


    /**
     * Lightens a color by a given factor.
     *
     * @param color: The color to lighten
     * @param factor: The factor to lighten the color. 0 will make the color unchanged.
     * 1 will make the color white.
     * @return lighter version of the specified color.
     */
    @JvmStatic
    fun lighter(color: Int, factor: Float): Int {
        val red = ((Color.red(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val green = ((Color.green(color) * (1 - factor) / 255 + factor) * 255).toInt()
        val blue = ((Color.blue(color) * (1 - factor) / 255 + factor) * 255).toInt()
        return Color.argb(Color.alpha(color), red, green, blue)
    }
}
