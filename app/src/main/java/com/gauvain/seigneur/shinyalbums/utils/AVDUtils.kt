package com.gauvain.seigneur.shinyalbums.utils

import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

object AVDUtils {

    fun startLoadingAnimation(imageView: AppCompatImageView, start: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val loaderAvd = imageView.drawable as AnimatedVectorDrawable
            if (start) {
                AnimatedVectorDrawableCompat.registerAnimationCallback(
                    loaderAvd,
                    object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            imageView.post {
                                imageView.startVectorAnimation()
                            }
                        }
                    })
                imageView.startVectorAnimation()
            } else {
                imageView.stopVectorAnimation()
            }
        } else {
            val loaderAvd = imageView.drawable as Animatable2Compat
            if (start) {
                loaderAvd.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        imageView.post { loaderAvd.start() }
                    }
                })
                loaderAvd.start()
            } else {
                loaderAvd.stop()
            }
        }
    }
}

fun ImageView.startVectorAnimation() {
    val avd = this.drawable as? Animatable
    avd?.start()
}

fun AppCompatImageView.stopVectorAnimation() {
    val avd = this.drawable as? Animatable
    avd?.stop()
}
