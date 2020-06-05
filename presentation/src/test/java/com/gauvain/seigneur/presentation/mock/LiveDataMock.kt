package com.gauvain.seigneur.presentation.mock

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.toDate
import com.gauvain.seigneur.presentation.model.AlbumItemData

object LiveDataMock {

    fun createAlbumPaginedList() = listOf<AlbumItemData>(
        AlbumItemData(
            id = 49201,
            albumTrackId = 49201,
            cover = "http://api.deezer.com/2.0/album/49201/image",
            title = "Groove Sessions",
            artistName = "Chinese Man",
            isExplicitLyrics = false
        )
    )

    fun createSuccessTracksOutCome() = Outcome.Success(
        TrackListModel(
            tracks = listOf(
                TrackModel(0L, true, "trackTitle", 20L, 1, "artistName", false)
            ),
            total = 1
        )
    )
}