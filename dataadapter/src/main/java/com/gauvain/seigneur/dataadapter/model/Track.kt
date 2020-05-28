package com.gauvain.seigneur.dataadapter.model

import com.gauvain.seigneur.domain.model.TrackListModel
import com.gauvain.seigneur.domain.model.TrackModel
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

data class TrackResponse(
    @SerializedName("data")
    val data: List<Track>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("error")
    override val errorResponse: ErrorResponse?=null
):BaseResponse()

fun TrackResponse.toModel() = TrackListModel(
    tracks = this.data.map {
        it.toModel()
    },
    total = this.total
)

data class Track(
    @SerializedName("id")
    val id: Long,
    @SerializedName("readeable")
    val isReadable: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("title_version")
    val titleVersion: String,
    @SerializedName("isrc")
    val isrc: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("duration")
    val duration: Long,
    @SerializedName("track_position")
    val trackPosition: Int,
    @SerializedName("disk_number")
    val diskNumber: Int,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("explicit_lyrics")
    val isExplicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Int,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("artist")
    val artist: TrackArtist
)

fun Track.toModel(): TrackModel = TrackModel(
    id = this.id,
    isReadable = this.isReadable,
    title = this.title,
    position = this.trackPosition,
    isExplicitLyrics = this.isExplicitLyrics,
    artist = this.artist.name,
    duration = TimeUnit.SECONDS.toMillis(this.duration)
)

data class TrackArtist(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("trackList")
    val trackList: String,
    @SerializedName("type")
    val type: String
)


