package com.gauvain.seigneur.dataadapter.mock

import com.gauvain.seigneur.dataadapter.model.Albums
import com.gauvain.seigneur.dataadapter.model.TrackResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object ResponseGsonObjectMock {

    fun createAlbumTracksSuccessResponse(): TrackResponse {
        val type = object : TypeToken<TrackResponse>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                    "data": [
                        {
                            "id": 313646,
                            "readable": true,
                            "title": "Chinese man",
                            "title_short": "Chinese man",
                            "title_version": "",
                            "isrc": "FR6V80312847",
                            "link": "http://www.deezer.com/track/313646",
                            "duration": 215,
                            "track_position": 12,
                            "disk_number": 1,
                            "rank": 384459,
                            "explicit_lyrics": false,
                            "explicit_content_lyrics": 0,
                            "explicit_content_cover": 2,
                            "preview": "http://cdn-preview-b.deezer.com/stream/c-b056f5edc553e92f2251a38b7ffea352-10.mp3",
                            "artist": {
                                "id": 58801,
                                "name": "Chinese Man",
                                "tracklist": "http://api.deezer.com/2.0/artist/58801/top?limit=50",
                                "type": "artist"
                            },
                            "type": "track"
                        }
                    ],
                    "total": 1
                }
                    """.trimIndent(), type
        )
    }

    fun createSuccessAlbumResponse(): Albums {
        val listType = object : TypeToken<Albums>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                    "data": [
                        {
                            "id": 49201,
                            "title": "Groove Sessions",
                            "link": "http://www.deezer.com/album/49201",
                            "cover": "http://api.deezer.com/2.0/album/49201/image",
                            "cover_small": "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/56x56-000000-80-0-0.jpg",
                            "cover_medium": "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/250x250-000000-80-0-0.jpg",
                            "cover_big": "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/500x500-000000-80-0-0.jpg",
                            "cover_xl": "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/1000x1000-000000-80-0-0.jpg",
                            "nb_tracks": 12,
                            "release_date": "2010-10-08",
                            "record_type": "album",
                            "available": true,
                            "tracklist": "http://api.deezer.com/2.0/album/49201/tracks",
                            "explicit_lyrics": false,
                            "time_add": 1359709023,
                            "artist": {
                                "id": 58801,
                                "name": "Chinese Man",
                                "picture": "http://api.deezer.com/2.0/artist/58801/image",
                                "picture_small": "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/56x56-000000-80-0-0.jpg",
                                "picture_medium": "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/250x250-000000-80-0-0.jpg",
                                "picture_big": "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/500x500-000000-80-0-0.jpg",
                                "picture_xl": "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/1000x1000-000000-80-0-0.jpg",
                                "tracklist": "http://api.deezer.com/2.0/artist/58801/top?limit=50",
                                "type": "artist"
                            },
                            "type": "album"
                        }
                    ],
                    "checksum": "e0eb6dc95d7d21cad4489f4d7ca15843",
                    "total": 184,
                    "next": "http://api.deezer.com/2.0/user/2529/albums?index=25"
                }
                    """.trimIndent(), listType
        )
    }

    fun createTrackListErrorResponse(): TrackResponse {
        val wantedObject = object : TypeToken<TrackResponse>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                 "error": {
                       "type": "DataException",
                       "message": "no data",
                       "code": 800
                   }
                }
                    """.trimIndent(), wantedObject
        )
    }

    fun createErrorResponse(): Albums {
        val wantedObject = object : TypeToken<Albums>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                 "error": {
                       "type": "DataException",
                       "message": "no data",
                       "code": 800
                   }
                }
                    """.trimIndent(), wantedObject
        )
    }

    fun createNullBodyResponse(): Albums {
        val wantedObject = object : TypeToken<Albums>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                }
                    """.trimIndent(), wantedObject
        )
    }


    fun createTrackListNullBodyResponse(): TrackResponse {
        val wantedObject = object : TypeToken<TrackResponse>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                }
                    """.trimIndent(), wantedObject
        )
    }

}