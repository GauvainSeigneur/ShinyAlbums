package com.gauvain.seigneur.shinyalbums.views.userAlbums.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.NextRequestState
import com.gauvain.seigneur.presentation.model.NextRequestStatus
import com.gauvain.seigneur.shinyalbums.R
import com.gauvain.seigneur.shinyalbums.utils.AVDUtils.startLoadingAnimation
import kotlinx.android.synthetic.main.item_footer.view.*

class FooterViewHolder(
    itemView: View,
    private val retryListener: RetryListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_footer
    }

    fun bind(state: NextRequestState?) {
        with(itemView) {
            state?.let {
                when(it.nextRequestStatus) {
                    NextRequestStatus.RUNNING -> {
                        startLoadingAnimation(loadingView, true)
                        errorView.visibility = View.INVISIBLE
                    }
                    NextRequestStatus.SUCCESS -> {
                        startLoadingAnimation(loadingView, false)
                        loadingView.visibility = View.GONE
                        errorView.visibility = View.INVISIBLE
                    }
                    NextRequestStatus.FAILED -> {
                        startLoadingAnimation(loadingView, false)
                        loadingView.visibility = View.GONE
                        errorView.visibility = View.VISIBLE
                    }
                }
            }
            retryButton.setOnClickListener {
                retryListener.onRetry()
            }

        }
    }

}