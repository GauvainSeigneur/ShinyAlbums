package com.gauvain.seigneur.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.userAlbums.UserAlbumsViewModel
import com.gauvain.seigneur.presentation.utils.MainCoroutineRule
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
class UserAlbumsViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    // Run tasks synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @Mock
    private lateinit var useCase: GetUserAlbumsUseCase
    private lateinit var viewModel: UserAlbumsViewModel

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
    fun `given usecase return error when pagedList is observed then initialState must return error`() {
        mainCoroutineRule.runBlockingTest {
            viewModel = UserAlbumsViewModel(useCase)
            given(useCase.invoke("2529", 0)).willReturn(Outcome.Error(ErrorType.ERROR_BODY))
            viewModel.albumList.getOrAwaitValue()
            mainCoroutineRule.advanceUntilIdle()
            val value = viewModel.initialLoadingState?.getOrAwaitValue()
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
        }
    }
}