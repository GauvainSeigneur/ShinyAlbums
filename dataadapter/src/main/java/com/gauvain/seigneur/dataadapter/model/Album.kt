package com.gauvain.seigneur.dataadapter.model

import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.toDate
import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cover_small")
    val coverSmall: String,
    @SerializedName("cover_medium")
    val coverMedium: String,
    @SerializedName("cover_big")
    val coverBig: String,
    @SerializedName("cover_xl")
    val coverXL: String,
    @SerializedName("nb_tracks")
    val nbTracks: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("available")
    val available: Boolean,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("time_add")
    val timeAdd: Long,
    @SerializedName("type")
    val type: String,
    @SerializedName("artist")
    val artist: Artist
)

fun Album.toModel() = AlbumModel(
    id = this.id,
    title = this.title,
    link = this.link,
    cover = this.cover,
    coverSmall = this.coverSmall,
    coverMedium = this.coverMedium,
    coverBig = this.coverBig,
    coverXL = this.coverXL,
    nbTracks = this.nbTracks,
    releaseDate = this.releaseDate.toDate(SERVER_DATE_FORMAT),
    recordType = this.recordType,
    available = this.available,
    trackList = this.trackList,
    explicitLyrics = this.explicitLyrics,
    timeAdd = this.timeAdd.toDate(),
    type = this.type,
    artist = this.artist.toModel()
)
