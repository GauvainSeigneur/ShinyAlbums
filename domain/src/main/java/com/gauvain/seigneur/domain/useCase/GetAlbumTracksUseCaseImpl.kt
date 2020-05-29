package com.gauvain.seigneur.domain.useCase

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetAlbumTracksException
import com.gauvain.seigneur.domain.provider.GetAlbumTracksProvider

internal class GetAlbumTracksUseCaseImpl(private val provider: GetAlbumTracksProvider) :
    GetAlbumTracksUseCase {

    override suspend fun invoke(albumId: Long): Outcome<TrackListModel, ErrorType> {
        return try {
            val result = provider.getTracks(albumId)
            Outcome.Success(result)
        } catch (e: GetAlbumTracksException) {
            handleException(e)
        }
    }

    private fun handleException(e: GetAlbumTracksException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.BODY_NULL -> Outcome.Error(ErrorType.ERROR_BODY)
            RequestExceptionType.FORMATTED_ERROR -> Outcome.Error(ErrorType.ERROR_FORMATTED)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}