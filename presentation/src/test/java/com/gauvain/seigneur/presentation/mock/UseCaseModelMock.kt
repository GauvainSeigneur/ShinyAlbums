package com.gauvain.seigneur.presentation.mock

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.toDate

object UseCaseModelMock {

    fun createSuccessTracksOutCome() =  Outcome.Success(
        TrackListModel(
            tracks = listOf(
                TrackModel(0L, true, "trackTitle", 20L, 1, "artistName", false)
            ),
            total = 1
        )
    )

    fun createSuccessPaginedAlbumUseCaseOutCome() = Outcome.Success(
        AlbumPaginedModel(
            albums = listOf(
                AlbumModel(
                    id = 49201,
                    trackListId = 49201,
                    title = "Groove Sessions",
                    link = "http://www.deezer.com/album/49201",
                    cover = "http://api.deezer.com/2.0/album/49201/image",
                    coverSmall = "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/56x56-000000-80-0-0.jpg",
                    coverMedium = "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/250x250-000000-80-0-0.jpg",
                    coverBig = "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/500x500-000000-80-0-0.jpg",
                    coverXL = "http://e-cdn-images.deezer.com/images/cover/f093d4a9d85f50e91b0ae6b2119a72ef/1000x1000-000000-80-0-0.jpg",
                    nbTracks = 12,
                    releaseDate = "2010-10-08".toDate(SERVER_DATE_FORMAT),
                    recordType = "album",
                    available = true,
                    trackList = "http://api.deezer.com/2.0/album/49201/tracks",
                    explicitLyrics = false,
                    timeAdd = 1359709023L.toDate(),
                    type = "album",
                    artist = ArtistModel(
                        id = 58801,
                        name = "Chinese Man",
                        picture = "http://api.deezer.com/2.0/artist/58801/image",
                        pictureSmall = "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/56x56-000000-80-0-0.jpg",
                        pictureMedium = "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/250x250-000000-80-0-0.jpg",
                        pictureBig = "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/500x500-000000-80-0-0.jpg",
                        pictureXL = "http://e-cdn-images.deezer.com/images/artist/d3653b8d5608f93f8e190668d679bec4/1000x1000-000000-80-0-0.jpg",
                        trackList = "http://api.deezer.com/2.0/artist/58801/top?limit=50",
                        type = "artist"
                    )
                )
            ),
            next = null,
            prev = null
        )
    )

    fun createOutComeError()  = Outcome.Error(ErrorType.ERROR_UNKNOWN)
}