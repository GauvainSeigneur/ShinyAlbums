package com.gauvain.seigneur.shinyalbums.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.gauvain.seigneur.shinyalbums.R
import kotlinx.android.synthetic.main.view_album_summary.view.*

class DetailsSummaryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_album_summary, this)
    }

    fun handleAlbumInfoVisibility(ratio: Float, maxAlpha: Float) {
        collapsingCoverPlaceHolder.alpha = (ratio * maxAlpha)
        albumTitleTextView.alpha = (ratio * maxAlpha)
        albumArtistYearTextView.alpha = (ratio * maxAlpha)
    }
}