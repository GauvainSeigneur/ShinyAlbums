package com.gauvain.seigneur.dataadapter.mock

import com.gauvain.seigneur.dataadapter.model.Albums
import com.gauvain.seigneur.dataadapter.model.TrackResponse
import com.gauvain.seigneur.dataadapter.service.DeezerService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.Calls
import retrofit2.mock.MockRetrofit

object DeezerServiceMock {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.deezer.com/2.0/")
        .build()
    val behaviorDelegate: BehaviorDelegate<DeezerService> =
        MockRetrofit.Builder(retrofit).build().create(DeezerService::class.java)

    fun createServiceThatFail(t: Throwable) =
        object : DeezerService {
            override fun getUserAlbums(userId: String, index: Int): Call<Albums> {
                return behaviorDelegate.returning(Calls.failure<Throwable>(t)).getUserAlbums(userId, index)
            }

            override fun getAlbumTracks(albumId: Long): Call<TrackResponse> {
                return behaviorDelegate.returning(Calls.failure<Throwable>(t)).getAlbumTracks(albumId)
            }


        }

    fun createServiceWithResponses(stats: Any? = null) =
        object : DeezerService {
            override fun getUserAlbums(userId: String, index: Int): Call<Albums> {
                return behaviorDelegate.returningResponse(stats).getUserAlbums(userId, index)
            }

            override fun getAlbumTracks(albumId: Long): Call<TrackResponse> {
                return behaviorDelegate.returningResponse(stats).getAlbumTracks(albumId)
            }
        }
}