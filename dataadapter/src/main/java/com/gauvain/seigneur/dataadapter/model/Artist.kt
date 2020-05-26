package com.gauvain.seigneur.dataadapter.model

import com.gauvain.seigneur.domain.model.ArtistModel
import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("picture_small")
    val pictureSmall: String,
    @SerializedName("picture_medium")
    val pictureMedium: String,
    @SerializedName("picture_big")
    val pictureBig: String,
    @SerializedName("picture_xl")
    val pictureXL: String,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("type")
    val type: String
)

fun Artist.toModel() = ArtistModel(
    id = this.id,
    name = this.name,
    picture = this.picture,
    pictureSmall = this.pictureSmall,
    pictureMedium = this.pictureMedium,
    pictureBig = this.pictureBig,
    pictureXL = this.pictureXL,
    trackList = this.trackList,
    type = this.type
)