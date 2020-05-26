package com.gauvain.seigneur.domain.useCase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetUserAlbumsException
import com.gauvain.seigneur.domain.provider.GetUserAlbumsProvider

internal class GetUserAlbumsUseCaseImpl(private val provider: GetUserAlbumsProvider) :
    GetUserAlbumsUseCase {

    override suspend fun invoke(userId: String, page: Int): Outcome<AlbumPaginedModel, ErrorType> {
        return try {
            val result = provider.getUserAlbums(userId, page)
            Outcome.Success(result)
        } catch (e: GetUserAlbumsException) {
            handleException(e)
        }
    }

    private fun handleException(e: GetUserAlbumsException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST -> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }

}