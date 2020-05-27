package com.gauvain.seigneur.dataadapter.model

import com.gauvain.seigneur.domain.model.AlbumPaginedModel
import com.google.gson.annotations.SerializedName

const val INDEX_DELIMITER_VALUE = "index="

data class Albums(
    @SerializedName("data")
    val data: List<Album>,
    @SerializedName("checksum")
    val checkSum: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("prev")
    val prev: String? = null,
    override val errorResponse: ErrorResponse? = null
) : BaseResponse()

fun Albums.toModel(): AlbumPaginedModel = AlbumPaginedModel(
    albums = this.data.map {
        it.toModel()
    },
    prev = this.prev?.substringAfterLast(INDEX_DELIMITER_VALUE)?.toInt(),
    next = this.next?.substringAfterLast(INDEX_DELIMITER_VALUE)?.toInt()
)



