package com.gauvain.seigneur.dataadapter.adapter

import com.gauvain.seigneur.dataadapter.model.TrackResponse
import com.gauvain.seigneur.dataadapter.model.toModel
import com.gauvain.seigneur.dataadapter.service.DeezerService
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.TrackListModel
import com.gauvain.seigneur.domain.provider.GetAlbumTracksException
import com.gauvain.seigneur.domain.provider.GetAlbumTracksProvider
import retrofit2.Response

class GetAlbumTracksAdapter(private val service: DeezerService) :
    GetAlbumTracksProvider  {

    override fun getTracks(albumId: Long): TrackListModel {
        val result = runCatching {
            service.getAlbumTracks(albumId).execute()
        }.onFailure {
            throw GetAlbumTracksException(RequestExceptionType.UNKNOWN_ERROR, it.message)
        }
        return handleResult(result)
    }


    private fun handleResult(result: Result<Response<TrackResponse>>): TrackListModel{
        return result.run {
            getOrNull()?.body().let { response ->
                if (response?.errorResponse != null) {
                    throw GetAlbumTracksException(RequestExceptionType.FORMATTED_ERROR, response.errorResponse.message)
                } else {
                    response?.data?.let {
                        response.toModel()
                    }
                } ?: throw GetAlbumTracksException(RequestExceptionType.BODY_NULL, "Body null")
            }
        }
    }
}