package com.gauvain.seigneur.domain.model

data class AlbumPaginedModel(
    val albums: List<AlbumModel>,
    val next: Int?,
    val prev: Int?
)
