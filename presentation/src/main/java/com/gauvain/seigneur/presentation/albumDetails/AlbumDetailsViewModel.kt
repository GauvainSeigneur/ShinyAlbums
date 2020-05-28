package com.gauvain.seigneur.presentation.albumDetails

import android.util.Log
import androidx.lifecycle.*
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetAlbumTracksUseCase
import com.gauvain.seigneur.presentation.model.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

typealias SummaryState = LiveDataState<AlbumDetailsSummary>
typealias TracksState = LiveDataState<TrackData>

class AlbumDetailsViewModel(
    private val detailsData: AlbumDetailsData?,
    private val useCase: GetAlbumTracksUseCase
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }

    val tracksData = MutableLiveData<TracksState>()
    val summaryData = MutableLiveData<SummaryState>()
    val sharedTransitionData = MutableLiveData<SharedTransitionState>()

    fun getSummaryInfo() {
        detailsData?.let {
            summaryData.value = LiveDataState.Success(it.toSummary())
        }?: setNoDataError()
    }

    private fun setNoDataError() {
        summaryData.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }

    fun getAlbumTracks() {
        detailsData?.albumTrackListId?.let {
            viewModelScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    useCase.invoke(it)
                }
                when (result) {
                    is Outcome.Success -> {
                        tracksData.value = LiveDataState.Success(result.data.toTrackData())
                        Log.d("yop", "data ${result.data.toTrackData()}")
                    }
                    is Outcome.Error -> {
                    }
                }
            }
        } ?: setNoDataValue()
    }

    private fun setNoDataValue() {
        tracksData.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }
}