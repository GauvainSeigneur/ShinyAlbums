package com.gauvain.seigneur.shinyalbums.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gauvain.seigneur.shinyalbums.R
import com.gauvain.seigneur.shinyalbums.utils.AVDUtils
import com.gauvain.seigneur.shinyalbums.utils.startVectorAnimation
import kotlinx.android.synthetic.main.view_initial_loading.view.*
import kotlinx.android.synthetic.main.view_no_data_found.view.*

class InitialLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_initial_loading, this)
    }

    fun setLoaded() {
        showError(false)
        showLoader(false)
    }

    fun setLoading() {
        this.visibility = View.VISIBLE
        showError(false)
        showLoader(true)
    }

    fun setError(
        title: String?,
        desc: String?,
        buttonText: String?,
        @DrawableRes
        iconRes: Int?,
        retryListener: () -> Unit
    ) {
        this.visibility = View.VISIBLE
        setErrorInfo(title, desc)
        iconRes?.let {
            errorIcon.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
        }
        showError(true)
        showLoader(false)
        if (buttonText != null) {
            retryButton.text = buttonText
            retryButton.setOnClickListener { retryListener() }
        } else {
            retryButton.visibility = View.GONE
        }
    }

    private fun showLoader(isVisible: Boolean) {
        if (isVisible) {
            bigLoader.visibility = View.VISIBLE
            AVDUtils.startLoadingAnimation(bigLoader, true)
        } else {
            AVDUtils.startLoadingAnimation(bigLoader, false)
            bigLoader.visibility = View.GONE
        }
    }

    private fun showError(isVisible: Boolean) {
        if (isVisible) {
            errorView.visibility = View.VISIBLE
            errorIcon.startVectorAnimation()
        } else {
            errorView.visibility = View.GONE
        }
    }

    private fun setErrorInfo(title: String?, desc: String?) {
        errorTitle.text = title
        errorDesc.text = desc
    }
}