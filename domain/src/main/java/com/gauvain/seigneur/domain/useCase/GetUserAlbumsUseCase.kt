package com.gauvain.seigneur.domain.useCase

import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.GetUserAlbumsProvider

interface GetUserAlbumsUseCase {
    suspend fun invoke(userId: String, page: Int): Outcome<List<AlbumModel>, ErrorType>

    companion object {
        fun create(provider: GetUserAlbumsProvider): GetUserAlbumsUseCase =
            GetUserAlbumsUseCaseImpl(provider)
    }
}