package com.gauvain.seigneur.domain.useCase

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.TrackListModel
import com.gauvain.seigneur.domain.provider.GetAlbumTracksProvider

interface GetAlbumTracksUseCase {
    suspend fun invoke(albumId: Long): Outcome<TrackListModel, ErrorType>

    companion object {
        fun create(provider: GetAlbumTracksProvider): GetAlbumTracksUseCase =
            GetAlbumTracksUseCaseImpl(provider)
    }
}