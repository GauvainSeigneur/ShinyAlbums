package com.gauvain.seigneur.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetAlbumTracksUseCase
import com.gauvain.seigneur.presentation.albumDetails.AlbumDetailsViewModel
import com.gauvain.seigneur.presentation.mock.UseCaseModelMock
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.utils.MainCoroutineRule
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class AlbumDetailsViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    // Run tasks synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @Mock
    private lateinit var useCase: GetAlbumTracksUseCase
    private lateinit var viewModel: AlbumDetailsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
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
                AlbumDetailsSummary("coverUrl", "title", StringPresenter(   R.string.album_artist_year,
                    "artist",
                    "2020"))
            )
        )
    }

    @Test
    fun `Given AlbumDetailsData is false when fetchAlbumTracks then it must return error`() {
        viewModel = AlbumDetailsViewModel(albumDetailsData.copy(available = false), useCase)
        viewModel.fetchAlbumTracks()
        val value = viewModel.getTracksData().getOrAwaitValue()
        mainCoroutineRule.advanceUntilIdle()
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
        mainCoroutineRule.runBlockingTest {
            given(useCase.invoke(0L)).willReturn(Outcome.Error(ErrorType.ERROR_UNKNOWN))
            viewModel = AlbumDetailsViewModel(albumDetailsData, useCase)
            viewModel.fetchAlbumTracks()
            val loadingValue = viewModel.getLoadingState().getOrAwaitValue()
            assertEquals(
                loadingValue, LoadingState.IS_LOADING
            )
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
            val nextLoadingValue = viewModel.getLoadingState().getOrAwaitValue()
            assertEquals(
                nextLoadingValue, LoadingState.IS_LOADED
            )
        }
    }

    @Test
    fun `Given usecase return model when fetchAlbumTracks then it must return data`() {
        mainCoroutineRule.runBlockingTest {
            given(useCase.invoke(albumDetailsData.albumTrackListId)).willReturn(UseCaseModelMock.createSuccessTracksOutCome())
            viewModel = AlbumDetailsViewModel(albumDetailsData, useCase)
            viewModel.fetchAlbumTracks()
            val loadingValue = viewModel.getLoadingState().getOrAwaitValue()
            assertEquals(
                loadingValue, LoadingState.IS_LOADING
            )
            val value = viewModel.getTracksData().getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Success(
                    TrackData(
                        tracks = listOf(TrackItemData(0L, "trackTitle",  "artistName", false)),
                        total = QuantityStringPresenter(R.plurals.albums_tracks_duration, 1, 1)

                    )
                )
            )
            val nextLoadingValue = viewModel.getLoadingState().getOrAwaitValue()
            assertEquals(
                nextLoadingValue, LoadingState.IS_LOADED
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
