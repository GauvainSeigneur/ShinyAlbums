package com.gauvain.seigneur.shinyalbums.views.albumDetails

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gauvain.seigneur.presentation.albumDetails.AlbumDetailsViewModel
import com.gauvain.seigneur.presentation.model.AlbumDetailsData
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.SharedTransitionState
import com.gauvain.seigneur.shinyalbums.R
import com.gauvain.seigneur.shinyalbums.animation.TransitionListenerAdapter
import com.gauvain.seigneur.shinyalbums.utils.ImageUtils
import com.gauvain.seigneur.shinyalbums.utils.loadCover
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_album_details.*
import kotlinx.android.synthetic.main.view_album_summary.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumDetailsActivity : AppCompatActivity() {

    companion object {
        private const val FADE_MAX_VALUE = 1f
        private const val DARKEN_RATIO = 0.25f
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
            albumDetailsSummaryView.handleAlbumInfoVisibility(vRatio,  FADE_MAX_VALUE)
            detailsCoverCardView.y = collapsingCoverPlaceHolder.y + (verticalOffset * 0.5f)
            //*0.5 because we set the layout_collapseParallaxMultiplier to 0.5 for albumDetailsSummaryView
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
                    collapsingToolbar.title = it.data.albumTitle
                    albumArtistYearTextView.text = it.data.albumArtistAndYear.getFormattedString(
                        this
                    )
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
                    playFab.hide()
                    playFab.visibility = View.INVISIBLE
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
                    playFab.show()
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
            listenSharedEnterTransition()
        } else {
            viewModel.sharedTransitionData.value = SharedTransitionState.ENDED
        }
    }

    private fun loadCover(url: String) {
        detailsCoverImageView.loadCover(this, url, object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let {
                    adaptColorFromCover(ImageUtils.drawableToBitmap(it))
                }
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                adaptColorFromCover(ImageUtils.drawableToBitmap(detailsCoverImageView.drawable))
                return false
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

    private fun adaptColorFromCover(bitmap: Bitmap) {
        val p = Palette.from(bitmap).generate()
        val dominantSwatch = p.dominantSwatch
        // finally change the color
        dominantSwatch?.let {
            gradientBackground.setBackgroundColor(it.rgb)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //make this color a little darker than the background
                window.statusBarColor = ColorUtils.blendARGB(it.rgb, Color.BLACK, DARKEN_RATIO)
            }

        }
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
