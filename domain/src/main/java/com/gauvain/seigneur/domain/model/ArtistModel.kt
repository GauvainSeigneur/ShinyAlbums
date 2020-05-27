package com.gauvain.seigneur.domain.model

data class ArtistModel(
    val id: Long,
    val name: String,
    val picture: String,
    val pictureSmall: String,
    val pictureMedium: String,
    val pictureBig: String,
    val pictureXL: String,
    val trackList: String,
    val type: String
)