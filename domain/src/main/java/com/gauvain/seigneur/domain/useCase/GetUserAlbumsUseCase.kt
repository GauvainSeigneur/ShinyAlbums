package com.gauvain.seigneur.domain.useCase

import com.gauvain.seigneur.domain.model.AlbumPaginedModel
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.GetUserAlbumsProvider

interface GetUserAlbumsUseCase {
    fun invoke(userId: String, page: Int): Outcome<AlbumPaginedModel, ErrorType>

    companion object {
        fun create(provider: GetUserAlbumsProvider): GetUserAlbumsUseCase =
            GetUserAlbumsUseCaseImpl(provider)
    }
}