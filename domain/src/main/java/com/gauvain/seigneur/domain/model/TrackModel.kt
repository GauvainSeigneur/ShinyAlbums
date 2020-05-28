package com.gauvain.seigneur.domain.model

data class TrackListModel(
    val tracks: List<TrackModel>,
    val total: Int
)

data class TrackModel(
    val id: Long,
    val isReadable: Boolean,
    val title: String,
    val duration: Long,
    val position: Int,
    val artist: String,
    val isExplicitLyrics: Boolean
)
