package com.gauvain.seigneur.presentation.model

class NextRequestState {

    var nextRequestStatus: NextRequestStatus? = null
        private set
    var message: String = ""

    private constructor(nextRequestStatus: NextRequestStatus, message: String) {
        this.nextRequestStatus = nextRequestStatus
        this.message = message
    }

    private constructor(nextRequestStatus: NextRequestStatus) {
        this.nextRequestStatus = nextRequestStatus
    }

    companion object {
        var LOADED = NextRequestState(NextRequestStatus.SUCCESS)
        var LOADING = NextRequestState(NextRequestStatus.RUNNING)
        fun error(message: String?): NextRequestState {
            return NextRequestState(NextRequestStatus.FAILED, message ?: "unknown error")
        }
    }
}