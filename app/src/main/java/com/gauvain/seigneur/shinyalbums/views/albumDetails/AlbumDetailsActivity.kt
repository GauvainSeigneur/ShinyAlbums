package com.gauvain.seigneur.shinyalbums.views.albumDetails

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.gauvain.seigneur.shinyalbums.R
import android.transition.Transition
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.request.Request
import com.gauvain.seigneur.presentation.albumDetails.AlbumDetailsViewModel
import com.gauvain.seigneur.presentation.model.AlbumDetailsData
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.SharedTransitionState
import com.gauvain.seigneur.shinyalbums.animation.TransitionListenerAdapter
import com.gauvain.seigneur.shinyalbums.utils.loadCover
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_album_details.*
import kotlinx.android.synthetic.main.activity_album_details.appBarLayout
import kotlinx.android.synthetic.main.activity_album_details.initialLoadingView
import kotlinx.android.synthetic.main.activity_album_details.toolbar
import kotlinx.android.synthetic.main.view_album_summary.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumDetailsActivity : AppCompatActivity() {

    companion object {
        private const val FADE_MAX_VALUE = 1f
        private const val START_TRANSITION_DELAY = 125L // half transition delay
        private const val DETAILS_DATA = "details_data"
        fun newIntent(
            context: Context,
            data: AlbumDetailsData
        ): Intent =
            Intent(context, AlbumDetailsActivity::class.java)
                .putExtra(DETAILS_DATA, data)
    }

    private val trackListAdapter = TrackListAdapter()
    private var data: AlbumDetailsData? = null
    private val viewModel: AlbumDetailsViewModel by viewModel { parametersOf(data) }
    private val appBarOffsetListener: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val vTotalScrollRange = appBarLayout.totalScrollRange
            val vRatio = (vTotalScrollRange.toFloat() + verticalOffset) / vTotalScrollRange
            detailsHeaderContentView.alpha = (vRatio * FADE_MAX_VALUE)
            //*0.75 because we set the layout_collapseParallaxMultiplier to 0.25 for detailsHeaderContentView
            detailsCoverCardView.y = collapsingCoverPlaceHolder.y + (verticalOffset * 0.75f)
            detailsCoverCardView.alpha = (vRatio * FADE_MAX_VALUE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = intent.getParcelableExtra(DETAILS_DATA)
        viewModel.getSummaryInfo()
        setContentView(R.layout.activity_album_details)
        handleTransition()
        initViews()
        observe()
    }

    override fun onBackPressed() {
        viewModel.sharedTransitionData.value = SharedTransitionState.STARTED
        super.onBackPressed()
    }

    private fun initViews() {
        albumTracksRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailsActivity)
            adapter = trackListAdapter
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
    }

    private fun observe() {
        viewModel.summaryData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    loadCover(it.data.cover)
                    albumTitleTextView.text = it.data.albumTitle
                    albumArtistYearTextView.text = it.data.albumArtistAndYear.getFormattedString(this)
                }
                is LiveDataState.Error -> {
                    Toast.makeText(
                        this,
                        it.errorData.description?.getFormattedString(this),
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        })

        viewModel.sharedTransitionData.observe(this, Observer {
            when (it) {
                SharedTransitionState.STARTED -> {
                    detailsCoverCardView.alpha = 1f
                }
                SharedTransitionState.ENDED -> {
                    viewModel.getAlbumTracks()
                    recolorBackground()
                }
            }
        })

        viewModel.tracksData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    initialLoadingView.visibility = View.GONE
                    trackListAdapter.submitList(it.data.tracks)
                    trackNumberDurationTextView.text = it.data.total.getFormattedString(this)
                }
                is LiveDataState.Error -> {
                    initialLoadingView.setLoaded()
                    initialLoadingView.setError(
                        it.errorData.title?.getFormattedString(this),
                        it.errorData.description?.getFormattedString(this),
                        it.errorData.buttonText?.getFormattedString(this),
                        it.errorData.iconRes
                    ) { viewModel.getAlbumTracks() }
                }
            }
        })

        viewModel.trackLoadingState.observe(this, Observer {
            when (it) {
                LoadingState.IS_LOADING -> {
                    initialLoadingView.visibility = View.VISIBLE
                    initialLoadingView.setLoading()
                }
                LoadingState.IS_LOADED -> {
                    initialLoadingView.setLoaded()
                }
            }
        })
    }

    private fun handleTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Postpone the shared element enter transition.
            listenSharedEnterTransition()
        } else {
            recolorBackground()
        }
    }

    private fun loadCover(url: String?) {
        detailsCoverImageView.loadCover(url, object : Request.Listener {
            override fun onStart(request: Request) {
                super.onStart(request)
            }
        })
    }

    @RequiresApi(21)
    private fun listenSharedEnterTransition() {
        val sharedElementEnterTransition: Transition = window.sharedElementEnterTransition
        sharedElementEnterTransition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionStart(transition: Transition) {
                super.onTransitionStart(transition)
                viewModel.sharedTransitionData.value = SharedTransitionState.STARTED
            }

            override fun onTransitionEnd(transition: Transition) {
                super.onTransitionEnd(transition)
                viewModel.sharedTransitionData.value = SharedTransitionState.ENDED
            }
        })
    }

    private fun recolorBackground() {
        activityDetailsBackground.setBackgroundColor(
            ContextCompat.getColor(
                this@AlbumDetailsActivity,
                R.color.colorBackground
            )
        )
    }
}
