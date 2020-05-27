package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.AlbumPaginedModel
import com.gauvain.seigneur.domain.model.RequestExceptionType
import java.lang.Exception

interface GetUserAlbumsProvider {
    @Throws(GetUserAlbumsException::class)
    fun getUserAlbums(userId: String, page: Int): AlbumPaginedModel
}

class GetUserAlbumsException(
    val type: RequestExceptionType,
    val description: String? = null
) : Exception()
