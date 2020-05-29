package com.gauvain.seigneur.presentation.model

import android.os.Parcelable
import android.view.View
import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.utils.YEAR_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter
import kotlinx.android.parcel.Parcelize

/**
 * Data displayed while album tracks are loaded
 */
data class AlbumDetailsSummary(
    val cover: String,
    val albumTitle: String,
    val albumArtistAndYear: StringPresenter
)

/**
 * Used by viewModel to trigger an event to display details
 */
data class TransitionAlbumItemData(
    val details: AlbumDetailsData,
    val rootView: View,
    val cardView: View,
    val imageView: View
)

/**
 * Data sent into an intent between album and details activity
 */
@Parcelize
data class AlbumDetailsData(
    val id: Long,
    val albumTrackListId: Long,
    val available: Boolean,
    val cover: String,
    val title: String,
    val artistName: String,
    val releaseYear: String,
    val trackNumber: Int
) : Parcelable

fun AlbumDetailsData.toSummary() = AlbumDetailsSummary(
    cover = this.cover,
    albumTitle = this.title,
    albumArtistAndYear = StringPresenter(
        R.string.album_artist_year,
        this.artistName,
        this.releaseYear
    )
)

fun AlbumModel.toAlbumDetailData(): AlbumDetailsData = AlbumDetailsData(
    id = this.id,
    albumTrackListId = this.trackListId,
    title = this.title,
    artistName = this.artist.name,
    cover = this.coverBig,
    releaseYear = this.releaseDate.formatTo(YEAR_FORMAT),
    trackNumber = this.nbTracks,
    available = this.available
)






