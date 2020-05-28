package com.gauvain.seigneur.dataadapter.adapter

import android.util.Log
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
            Log.d("getTracks", "oops $it")
            throw GetAlbumTracksException(RequestExceptionType.UNKNOWN_ERROR, it.message)
        }
        return handleResult(result)
    }


    private fun handleResult(result: Result<Response<TrackResponse>>): TrackListModel{
        return result.run {
            getOrNull()?.body().let { response ->
                if (response?.errorResponse != null) {
                    //73561
                    //71171
                    Log.d("getTracks", "null $response")
                    throw GetAlbumTracksException(RequestExceptionType.FORMATTED_ERROR, response.errorResponse.message)
                } else {
                    response?.data?.let {
                        Log.d("getTracks", "data $it")
                        response.toModel()
                    }
                } ?: throw GetAlbumTracksException(RequestExceptionType.BODY_NULL, "Body null")
            }
        }
    }
}