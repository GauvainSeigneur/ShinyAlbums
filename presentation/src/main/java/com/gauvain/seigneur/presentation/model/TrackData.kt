package com.gauvain.seigneur.presentation.model

import com.gauvain.seigneur.domain.model.TrackListModel
import com.gauvain.seigneur.domain.model.TrackModel
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.utils.StringPresenter
import java.util.concurrent.TimeUnit

data class TrackData(
   val tracks: List<TrackItemData>,
   val total: StringPresenter
)

fun TrackListModel.toTrackData() = TrackData(
   tracks = this.tracks.map {
      it.toData()
   },
   total = StringPresenter(R.string.total_tracks, this.total)
)

data class TrackItemData(
    val id: Long,
    val title: String,
    val artistName: String,
    val isExplicitLyrics: Boolean,
    val duration: String
)

fun TrackModel.toData(): TrackItemData = TrackItemData(
    id = this.id,
    title = this.title,
    artistName = this.artist,
    isExplicitLyrics = this.isExplicitLyrics,
    duration = convertLongToDurationString(this.duration)
)

private fun convertLongToDurationString(duration: Long):String =
   String.format(
      "%02d min, %02d sec",
      TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) -
          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
   )


