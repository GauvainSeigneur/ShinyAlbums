package com.gauvain.seigneur.presentation

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetAlbumTracksUseCase
import com.gauvain.seigneur.presentation.albumDetails.AlbumDetailsViewModel
import com.gauvain.seigneur.presentation.mock.UseCaseModelMock
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.utils.*
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class AlbumDetailsViewModelTest : BaseViewModelTest() {

    // Run tasks synchronously
    @Mock
    private lateinit var useCase: GetAlbumTracksUseCase
    private lateinit var viewModel: AlbumDetailsViewModel

    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    @Test
    fun `Given AlbumDetailsData is null when observe summarydata then it must return errorData`() {
        viewModel = AlbumDetailsViewModel(null, useCase)
        val value = viewModel.getSummaryData().getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Error(
                ErrorData(ErrorDataType.NOT_RECOVERABLE)
            )
        )
    }

    @Test
    fun `Given AlbumDetailsData is not null when observe summarydata then it must return data`() {
        viewModel = AlbumDetailsViewModel(albumDetailsData, useCase)
        val value = viewModel.getSummaryData().getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Success(
                AlbumDetailsSummary(
                    "coverUrl", "title", StringPresenter(
                        R.string.album_artist_year,
                        "artist",
                        "2020"
                    )
                )
            )
        )
    }

    @Test
    fun `Given AlbumDetailsData is false when fetchAlbumTracks then it must return error`() {
        viewModel = AlbumDetailsViewModel(albumDetailsData.copy(available = false), useCase)
        viewModel.fetchAlbumTracks()
        val value = viewModel.getTracksData().getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Error(
                ErrorData(
                    ErrorDataType.INFORMATIVE,
                    StringPresenter(R.string.error_tracks_not_available_title),
                    StringPresenter(R.string.error_tracks_not_available_description),
                    null,
                    R.drawable.ic_tracks_not_available
                )
            )
        )
    }

    @Test
    fun `Given an exception is raised when fetchAlbumTracks then it must return error`() {
        runBlockingTest {
            given(useCase.invoke(0L)).willReturn(Outcome.Error(ErrorType.ERROR_UNKNOWN))
            viewModel = AlbumDetailsViewModel(albumDetailsData, useCase)
            unconfinifyTestScope()
            viewModel.fetchAlbumTracks()
            val loadingValue = viewModel.getLoadingState().getOrAwaitValue()
            val value = viewModel.getTracksData().getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.RECOVERABLE,
                        StringPresenter(R.string.error_fetch_data_title),
                        StringPresenter(R.string.error_fetch_data_description),
                        StringPresenter(R.string.retry)
                    )
                )
            )
            assertEquals(
                loadingValue, LoadingState.IS_LOADED
            )
        }
    }

    @Test
    fun `Given usecase return model when fetchAlbumTracks then it must return data`() {
        runBlockingTest {
            given(useCase.invoke(albumDetailsData.albumTrackListId)).willReturn(UseCaseModelMock.createSuccessTracksOutCome())
            viewModel = AlbumDetailsViewModel(albumDetailsData, useCase)
            unconfinifyTestScope()
            viewModel.fetchAlbumTracks()
            val loadingValue = viewModel.getLoadingState().getOrAwaitValue()
            val value = viewModel.getTracksData().getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Success(
                    TrackData(
                        tracks = listOf(TrackItemData(0L, "trackTitle", "artistName", false)),
                        total = QuantityStringPresenter(R.plurals.albums_tracks_duration, 1, 1)
                    )
                )
            )
            assertEquals(
                loadingValue, LoadingState.IS_LOADED
            )
        }
    }

    private val albumDetailsData = AlbumDetailsData(
        0L,
        0L,
        true,
        "coverUrl",
        "title",
        "artist",
        "2020",
        10
    )
}
