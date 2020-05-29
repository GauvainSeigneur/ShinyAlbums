package com.gauvain.seigneur.shinyalbums.views.userAlbums

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gauvain.seigneur.presentation.model.AlbumDetailsData
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.NextRequestState
import com.gauvain.seigneur.presentation.userAlbums.UserAlbumsViewModel
import com.gauvain.seigneur.presentation.utils.event.EventObserver
import com.gauvain.seigneur.shinyalbums.R
import com.gauvain.seigneur.shinyalbums.animation.makeSceneTransitionAnimation
import com.gauvain.seigneur.shinyalbums.views.albumDetails.AlbumDetailsActivity
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

    private val viewModel: UserAlbumsViewModel by viewModel()
    private lateinit var adapter: UserAlbumListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_albums)
        initAdapter()
        observeViewModel()
    }

    override fun onClick(id: Long?, rootView: View, cardView: View, imageView: View) {
        viewModel.getAlbumDetail(id, rootView, cardView, imageView)
    }

    private fun displayDetails(
        details: AlbumDetailsData,
        rootView: View,
        cardView: View,
        imageView: View
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = makeSceneTransitionAnimation(
                this@UserAlbumsActivity,
                Pair(rootView, getString(R.string.transition_root)),
                Pair(cardView, getString(R.string.transition_country_flag)),
                Pair(imageView, getString(R.string.transition_cover_image))
            )
            startActivity(
                AlbumDetailsActivity.newIntent(this@UserAlbumsActivity, details),
                options.toBundle()
            )
        } else {
            startActivity(
                AlbumDetailsActivity.newIntent(this@UserAlbumsActivity, details)
            )
        }
    }

    override fun onRetry() {
        viewModel.retry()
    }

    private fun observeViewModel() {
        viewModel.albumList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.initialLoadingState?.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    when (it.data) {
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
                        it.errorData.buttonText?.getFormattedString(this),
                        it.errorData.iconRes
                    ) { viewModel.retry() }
                }
            }
        })

        viewModel.nextLoadingState?.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    when (it.data) {
                        LoadingState.IS_LOADED -> {
                            adapter.setNetworkState(NextRequestState.LOADED)
                        }
                        LoadingState.IS_LOADING -> {
                            adapter.setNetworkState(NextRequestState.LOADING)
                        }
                    }
                }
                is LiveDataState.Error -> {
                    adapter.setNetworkState(
                        NextRequestState.error(
                            it.errorData.description?.getFormattedString(
                                this
                            )
                        )
                    )
                }
            }
        })

        viewModel.displaysDetailsEvent.observe(this, EventObserver { result ->
            when (result) {
                is LiveDataState.Success -> {
                    displayDetails(
                        result.data.details,
                        result.data.rootView,
                        result.data.cardView,
                        result.data.imageView
                    )
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
        userAlbumsRecyclerView.adapter = adapter
    }
}
