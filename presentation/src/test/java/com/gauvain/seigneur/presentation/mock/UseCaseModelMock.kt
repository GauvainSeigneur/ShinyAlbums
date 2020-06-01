package com.gauvain.seigneur.presentation.mock

import com.gauvain.seigneur.domain.model.*

object UseCaseModelMock {

    fun createSuccessTracksOutCome() = Outcome.Success(
        TrackListModel(
            tracks = listOf(
                TrackModel(0L, true, "trackTitle", 20L, 1, "artistName", false)
            ),
            total = 1
        )
    )
}