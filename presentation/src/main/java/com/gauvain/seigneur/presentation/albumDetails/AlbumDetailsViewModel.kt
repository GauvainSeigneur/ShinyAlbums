package com.gauvain.seigneur.presentation.albumDetails

import androidx.lifecycle.*
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetAlbumTracksUseCase
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.ioJob

typealias SummaryState = LiveDataState<AlbumDetailsSummary>
typealias TracksState = LiveDataState<TrackData>

class AlbumDetailsViewModel(
    private val detailsData: AlbumDetailsData?,
    private val useCase: GetAlbumTracksUseCase
) : ViewModel() {

    private val sharedTransitionData = MutableLiveData<SharedTransitionState>()
    private val trackLoadingState = MutableLiveData<LoadingState>()
    private val tracksData = MutableLiveData<TracksState>()
    private val summaryData: MutableLiveData<SummaryState> by lazy {
        MutableLiveData<SummaryState>().also { livedata ->
            getSummaryInfo(livedata)
        }
    }

    fun getSummaryData(): LiveData<SummaryState> = summaryData
    fun getLoadingState(): LiveData<LoadingState> = trackLoadingState
    fun getTracksData(): LiveData<TracksState> = tracksData
    fun getSharedTransitionData(): LiveData<SharedTransitionState> = sharedTransitionData
    fun setSharedTransitionStarted() {
        sharedTransitionData.value = SharedTransitionState.STARTED
    }

    fun setSharedTransitionEnded() {
        sharedTransitionData.value = SharedTransitionState.ENDED
    }

    private fun getSummaryInfo(data: MutableLiveData<SummaryState>) {
        detailsData?.let {
            data.value = LiveDataState.Success(it.toSummary())
        } ?: setNoDataError(data)
    }

    private fun setNoDataError(data: MutableLiveData<SummaryState>) {
        data.value = LiveDataState.Error(ErrorData(ErrorDataType.NOT_RECOVERABLE))
    }

    fun fetchAlbumTracks() {
        detailsData?.let {
            if (it.available) {
                fetchAlbumTracks(it.albumTrackListId)
            } else {
                setNotAvailableValue()
            }
        } ?: setNoDataValue()
    }

    private fun fetchAlbumTracks(id: Long) {
        ioJob {
            trackLoadingState.postValue(LoadingState.IS_LOADING)
            val result = useCase.invoke(id)
            trackLoadingState.postValue(LoadingState.IS_LOADED)
            when (result) {
                is Outcome.Success -> {
                    tracksData.postValue(LiveDataState.Success(result.data.toTrackData()))
                }
                is Outcome.Error -> {
                    tracksData.postValue(
                        LiveDataState.Error(
                            ErrorData(
                                ErrorDataType.RECOVERABLE,
                                StringPresenter(R.string.error_fetch_data_title),
                                StringPresenter(R.string.error_fetch_data_description),
                                StringPresenter(R.string.retry)
                            )
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