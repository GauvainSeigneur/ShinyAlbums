package com.gauvain.seigneur.dataadapter.adapter

import com.gauvain.seigneur.dataadapter.model.Albums
import com.gauvain.seigneur.dataadapter.model.toModel
import com.gauvain.seigneur.dataadapter.service.DeezerService
import com.gauvain.seigneur.domain.model.AlbumPaginedModel
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetUserAlbumsException
import com.gauvain.seigneur.domain.provider.GetUserAlbumsProvider
import retrofit2.Response

class GetUserAlbumsAdapter(private val service: DeezerService) :
    GetUserAlbumsProvider {

    override suspend fun getUserAlbums(userId: String, page: Int): AlbumPaginedModel {
        val result = runCatching {
            service.getUserAlbums(userId, page).execute()
        }.onFailure {
            throw GetUserAlbumsException(RequestExceptionType.UNKNOWN_ERROR, it.message)
        }
        return handleResult(result)
    }

    private fun handleResult(result: Result<Response<Albums>>): AlbumPaginedModel {
        return result.run {
            getOrNull()?.body().let { response ->
                if (response?.errorResponse != null) {
                    throw GetUserAlbumsException(
                        RequestExceptionType.FORMATTED_ERROR,
                        response.errorResponse.message
                    )
                } else {
                    response?.data?.let {
                        response.toModel()
                    }
                } ?: throw GetUserAlbumsException(RequestExceptionType.BODY_NULL, "Body null")
            }
        }
    }
}