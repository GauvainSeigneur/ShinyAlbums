package com.gauvain.seigneur.shinyalbums

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.NextRequestState
import com.gauvain.seigneur.presentation.model.NextRequestStatus

class FooterViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val layout = R.layout.item_footer
    }

    fun bind(state: NextRequestState?) {
        with(itemView) {
            state?.let {
                when(it.nextRequestStatus) {
                    NextRequestStatus.RUNNING -> {}
                    NextRequestStatus.SUCCESS -> {}
                    NextRequestStatus.FAILED -> {}
                }
            }

        }
    }

}