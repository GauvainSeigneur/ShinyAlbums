package com.gauvain.seigneur.presentation.model

import com.gauvain.seigneur.domain.model.TrackListModel
import com.gauvain.seigneur.domain.model.TrackModel
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter
import java.util.concurrent.TimeUnit

data class TrackData(
   val tracks: List<TrackItemData>,
   val total: QuantityStringPresenter
)

fun TrackListModel.toTrackData() = TrackData(
   tracks = this.tracks.map {
      it.toData()
   },
   total = QuantityStringPresenter(R.plurals.albums_tracks_duration,
       this.total, this.total)
)

data class TrackItemData(
    val id: Long,
    val title: String,
    val artistName: String,
    val isExplicitLyrics: Boolean
)

fun TrackModel.toData(): TrackItemData = TrackItemData(
    id = this.id,
    title = this.title,
    artistName = this.artist,
    isExplicitLyrics = this.isExplicitLyrics
)



