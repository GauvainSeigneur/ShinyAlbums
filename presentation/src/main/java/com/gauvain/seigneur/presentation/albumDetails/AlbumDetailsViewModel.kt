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
        }?: setNoDataError()
    }

    private fun setNoDataError() {
        summaryData.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }

    fun getAlbumTracks() {
        detailsData?.albumTrackListId?.let {
            trackLoadingState.value = LoadingState.IS_LOADING
            viewModelScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    useCase.invoke(it)
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
        } ?: setNoDataValue()
    }

    private fun setNoDataValue() {
        tracksData.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }
}