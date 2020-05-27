package com.gauvain.seigneur.domain.model

import java.util.*

data class AlbumModel(
    val id: Long,
    val title: String,
    val link: String,
    val cover: String,
    val coverSmall: String,
    val coverMedium: String,
    val coverBig: String,
    val coverXL: String,
    val nbTracks: Int,
    val releaseDate: Date,
    val recordType: String,
    val available: Boolean,
    val trackList: String,
    val explicitLyrics: Boolean,
    val timeAdd: Date,
    val type: String,
    val artist: ArtistModel
)
