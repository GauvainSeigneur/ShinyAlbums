package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.TrackListModel
import java.lang.Exception

interface GetAlbumTracksProvider {
    @Throws(GetUserAlbumsException::class)
    fun getTracks(albumId: Long): TrackListModel
}

class GetAlbumTracksException(
    val type: RequestExceptionType,
    val description: String? = null
) : Exception()
