package com.gauvain.seigneur.presentation.albumDetails

import androidx.lifecycle.*
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetAlbumTracksUseCase
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.utils.StringPresenter
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

    val trackLoadingState = MutableLiveData<LoadingState>()
    val tracksData = MutableLiveData<TracksState>()
    val summaryData = MutableLiveData<SummaryState>()
    val sharedTransitionData = MutableLiveData<SharedTransitionState>()

    fun getSummaryInfo() {
        detailsData?.let {
            summaryData.value = LiveDataState.Success(it.toSummary())
        } ?: setNoDataError()
    }

    private fun setNoDataError() {
        summaryData.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }

    fun getAlbumTracks() {
        detailsData?.let {
            if (it.available) {
                fetchAlbumTracks(it.albumTrackListId)
            } else {
                setNotAvailableValue()
            }
        } ?: setNoDataValue()
    }

    private fun fetchAlbumTracks(id: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            trackLoadingState.value = LoadingState.IS_LOADING
            val result = withContext(Dispatchers.IO) {
                useCase.invoke(id)
            }
            trackLoadingState.value = LoadingState.IS_LOADED
            when (result) {
                is Outcome.Success -> {
                    tracksData.value = LiveDataState.Success(result.data.toTrackData())
                }
                is Outcome.Error -> {
                    tracksData.value = LiveDataState.Error(
                        ErrorData(
                            ErrorDataType.RECOVERABLE,
                            StringPresenter(R.string.error_fetch_data_title),
                            StringPresenter(R.string.error_fetch_data_description),
                            StringPresenter(R.string.retry)
                        )
                    )
                }
            }
        }
    }

    private fun setNoDataValue() {
        tracksData.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }

    private fun setNotAvailableValue() {
        tracksData.value = LiveDataState.Error(
            ErrorData(
                ErrorDataType.INFORMATIVE,
                StringPresenter(R.string.error_tracks_not_available_title),
                StringPresenter(R.string.error_tracks_not_available_description),
                null,
                R.drawable.ic_tracks_not_available
            )
        )
    }
}