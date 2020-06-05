package com.gauvain.seigneur.domain.useCase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetUserAlbumsException
import com.gauvain.seigneur.domain.provider.GetUserAlbumsProvider

internal class GetUserAlbumsUseCaseImpl(private val provider: GetUserAlbumsProvider) :
    GetUserAlbumsUseCase {

    override fun invoke(userId: String, page: Int): Outcome<AlbumPaginedModel, ErrorType> {
        return try {
            val result = provider.getUserAlbums(userId, page)
            Outcome.Success(result)
        } catch (e: GetUserAlbumsException) {
            handleException(e)
        }
    }

    private fun handleException(e: GetUserAlbumsException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.BODY_NULL -> Outcome.Error(ErrorType.ERROR_BODY)
            RequestExceptionType.FORMATTED_ERROR -> Outcome.Error(ErrorType.ERROR_FORMATTED)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}