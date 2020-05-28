package com.gauvain.seigneur.dataadapter.service

import com.gauvain.seigneur.dataadapter.model.Albums
import com.gauvain.seigneur.dataadapter.model.TrackResponse
import retrofit2.Call
import retrofit2.http.*

interface DeezerService {
    @GET("user/{userId}/albums")
    fun getUserAlbums(
        @Path(value = "userId", encoded = true)
        userId: String,
        @Query("index")
        index: Int
    ): Call<Albums>


    @GET("album/{albumId}/tracks")
    fun getAlbumTracks(
        @Path(value = "albumId", encoded = true)
        albumId: Long
    ): Call<TrackResponse>
}

