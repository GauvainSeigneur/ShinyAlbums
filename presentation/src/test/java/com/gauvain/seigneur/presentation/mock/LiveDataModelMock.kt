package com.gauvain.seigneur.presentation.mock

import com.gauvain.seigneur.presentation.model.AlbumItemData

object LiveDataModelMock {

    fun createAlbumsPaginedLiveData() = listOf<AlbumItemData>(
        AlbumItemData(
            id = 49201L,
            cover = "http://api.deezer.com/2.0/album/49201/image",
            title = "Groove Sessions",
            artistName = "Chinese Man",
            isExplicitLyrics = false
        )
    )

    fun emptyList() = listOf<AlbumItemData>()
}