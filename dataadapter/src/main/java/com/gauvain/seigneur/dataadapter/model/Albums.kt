package com.gauvain.seigneur.dataadapter.model

import com.google.gson.annotations.SerializedName

data class Albums(
    @SerializedName("data")
    val data: List<Album>,
    @SerializedName("checksum")
    val checkSum: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("next")
    val next: String?=null,
    @SerializedName("prev")
    val prev: String?=null,
    override val errorResponse: ErrorResponse? = null
) : BaseResponse()

/*
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
        },
 */

/*
            "alternative": {
                "id": 9421290,
                "title": "Carnavas",
                "link": "http://www.deezer.com/album/9421290",
                "cover": "http://api.deezer.com/2.0/album/9421290/image",
                "cover_small": "http://e-cdn-images.deezer.com/images/cover/05b51e27ce353b46301d77ac32ffdd77/56x56-000000-80-0-0.jpg",
                "cover_medium": "http://e-cdn-images.deezer.com/images/cover/05b51e27ce353b46301d77ac32ffdd77/250x250-000000-80-0-0.jpg",
                "cover_big": "http://e-cdn-images.deezer.com/images/cover/05b51e27ce353b46301d77ac32ffdd77/500x500-000000-80-0-0.jpg",
                "cover_xl": "http://e-cdn-images.deezer.com/images/cover/05b51e27ce353b46301d77ac32ffdd77/1000x1000-000000-80-0-0.jpg",
                "nb_tracks": 11,
                "release_date": "2006-07-25",
                "record_type": "album",
                "available": true,
                "tracklist": "http://api.deezer.com/2.0/album/9421290/tracks",
                "explicit_lyrics": false,
                "artist": {
                    "id": 8466,
                    "name": "Silversun Pickups",
                    "picture": "http://api.deezer.com/2.0/artist/8466/image",
                    "picture_small": "http://e-cdn-images.deezer.com/images/artist/d0eeec849ebce6a3f347e592f5659bf2/56x56-000000-80-0-0.jpg",
                    "picture_medium": "http://e-cdn-images.deezer.com/images/artist/d0eeec849ebce6a3f347e592f5659bf2/250x250-000000-80-0-0.jpg",
                    "picture_big": "http://e-cdn-images.deezer.com/images/artist/d0eeec849ebce6a3f347e592f5659bf2/500x500-000000-80-0-0.jpg",
                    "picture_xl": "http://e-cdn-images.deezer.com/images/artist/d0eeec849ebce6a3f347e592f5659bf2/1000x1000-000000-80-0-0.jpg",
                    "tracklist": "http://api.deezer.com/2.0/artist/8466/top?limit=50",
                    "type": "artist"
                },
                "type": "album"
            },
 */




