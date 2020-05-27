package com.gauvain.seigneur.shinyalbums.views.userAlbums

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.NextRequestState
import com.gauvain.seigneur.presentation.viewModel.UserAlbumsViewModel
import com.gauvain.seigneur.shinyalbums.R
import com.gauvain.seigneur.shinyalbums.views.userAlbums.list.ItemClickListener
import com.gauvain.seigneur.shinyalbums.views.userAlbums.list.RetryListener
import com.gauvain.seigneur.shinyalbums.views.userAlbums.list.UserAlbumListAdapter
import kotlinx.android.synthetic.main.activity_user_albums.*
import org.koin.android.viewmodel.ext.android.viewModel

class UserAlbumsActivity : AppCompatActivity(),
    ItemClickListener, RetryListener {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, UserAlbumsActivity::class.java)
    }

    private val viewModel : UserAlbumsViewModel by viewModel()
    private lateinit var adapter: UserAlbumListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_albums)
        initAdapter()
        observeViewModel()
    }

    override fun onClick(id: Long?) {

    }

    override fun onRetry() {
        viewModel.retry()
    }

    private fun observeViewModel() {
        viewModel.albumList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.initialLoadingState?.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    when(it.data) {
                        LoadingState.IS_LOADED -> {
                            initialLoadingView.setLoaded()
                            initialLoadingView.visibility = View.GONE
                        }
                        LoadingState.IS_LOADING -> {
                            initialLoadingView.setLoading()
                        }
                    }
                }
                is LiveDataState.Error -> {
                    initialLoadingView.setError(
                        it.errorData.title?.getFormattedString(this),
                        it.errorData.description?.getFormattedString(this),
                        it.errorData.buttonText?.getFormattedString(this)
                    ) { viewModel.retry() }
                }
            }
        })

        viewModel.nextLoadingState?.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    when(it.data) {
                        LoadingState.IS_LOADED -> {
                            adapter.setNetworkState(NextRequestState.LOADED)
                        }
                        LoadingState.IS_LOADING -> {
                            adapter.setNetworkState(NextRequestState.LOADING)
                        }
                    }
                }
                is LiveDataState.Error -> {
                    adapter.setNetworkState(NextRequestState.error(it.errorData.description?.getFormattedString(this)))
                }
            }

        })

    }

    private fun initAdapter() {
        adapter = UserAlbumListAdapter(
            this,
            this
        )

        userAlbumsRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL,
            false
        )
        /*serAlbumsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )*/
        userAlbumsRecyclerView.adapter = adapter
    }

}
