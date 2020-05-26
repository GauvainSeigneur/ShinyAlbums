package com.gauvain.seigneur.dataadapter.model

import com.google.gson.annotations.SerializedName

abstract class BaseResponse {
    abstract val errorResponse: ErrorResponse?
}

data class ErrorResponse(
    @SerializedName("type")
    val type : String,
    @SerializedName("message")
    val message : String,
    @SerializedName("code")
    val code : Int
)


