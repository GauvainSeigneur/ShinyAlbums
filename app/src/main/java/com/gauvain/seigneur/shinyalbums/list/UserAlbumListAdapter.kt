package com.gauvain.seigneur.shinyalbums.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.AlbumItemData
import com.gauvain.seigneur.presentation.model.NextRequestState

class UserAlbumListAdapter(
    private val listener: ItemClickListener,
    private val retryListener: RetryListener
) : PagedListAdapter<AlbumItemData, RecyclerView.ViewHolder>
    (DiffCallback) {

    private var nextRequestState: NextRequestState? = null

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1

        val DiffCallback = object : DiffUtil.ItemCallback<AlbumItemData>() {
            override fun areItemsTheSame(oldItem: AlbumItemData, newItem: AlbumItemData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlbumItemData, newItem: AlbumItemData): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ITEM -> return UserAlbumViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    UserAlbumViewHolder.layout,
                    parent,
                    false
                )
            )
            LOADING -> return FooterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    FooterViewHolder.layout,
                    parent,
                    false
                ),
                retryListener
            )
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> (holder as UserAlbumViewHolder).bind(getItem(position), listener)
            LOADING -> (holder as FooterViewHolder).bind(nextRequestState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            LOADING
        } else {
            ITEM
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }


    private fun hasExtraRow(): Boolean {
        return nextRequestState != null && nextRequestState != NextRequestState.LOADED
    }


    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNextRequestState the new network state
     */
    fun setNetworkState(newNextRequestState: NextRequestState) {
        //todo - issue with refresh action: after rferesh the recyclerview focus at the bottom
        //to fix this : check if tis is initial load or not
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.nextRequestState
                val hadExtraRow = hasExtraRow()
                this.nextRequestState = newNextRequestState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState != newNextRequestState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }
}
