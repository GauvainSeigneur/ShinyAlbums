package com.gauvain.seigneur.dataadapter.service

import com.gauvain.seigneur.dataadapter.model.Albums
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
}