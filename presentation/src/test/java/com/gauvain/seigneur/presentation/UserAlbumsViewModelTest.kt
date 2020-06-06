package com.gauvain.seigneur.presentation

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.mock.LiveDataMock
import com.gauvain.seigneur.presentation.mock.UseCaseModelMock
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.userAlbums.UserAlbumsViewModel
import com.gauvain.seigneur.presentation.utils.*
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class UserAlbumsViewModelTest:BaseViewModelTest() {

    @Mock
    private lateinit var useCase: GetUserAlbumsUseCase
    @InjectMocks
    private lateinit var viewModel: UserAlbumsViewModel


    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    @Test
    fun `albumList must return emptyList when observed`() {
        val listValue = viewModel.albumList.getOrAwaitValue()
        assertEquals(listValue, listOf<AlbumItemData>())
    }

    @Test
    fun `given initialStateValue when pagedList is observed then initialState must return IS_LOADING`() {
        runBlockingTest {
            viewModel.albumList.getOrAwaitValue()
            val initialStatevalue = viewModel.initialLoadingState?.getOrAwaitValue()
            assertEquals(
                initialStatevalue, LiveDataState.Success(
                    LoadingState.IS_LOADING
                )
            )
        }
    }

    @Test
    fun `given usecase return error when pagedList is observed then initialState must return error`() {
        runBlockingTest {
            given(useCase.invoke("2529", 0)).willReturn(Outcome.Error(ErrorType.ERROR_BODY))
            val listValue = viewModel.albumList.getOrAwaitValue()
            val initialStatevalue = viewModel.initialLoadingState?.getOrAwaitValue()
            assertEquals(listValue, listOf<AlbumItemData>())
            assertEquals(
                initialStatevalue, LiveDataState.Error(
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

    @Test
    fun `given usecase return list when pagedList is observed then must return data`() {
        runBlockingTest {
            given(
                useCase.invoke(
                    "2529",
                    0
                )
            ).willReturn(UseCaseModelMock.createSuccessAlbumOutCome())
            val listValue = viewModel.albumList.getOrAwaitValue()
            val initialStatevalue = viewModel.initialLoadingState?.getOrAwaitValue()
            assertEquals(listValue, LiveDataMock.createAlbumPaginedList())
            assertEquals(
                initialStatevalue,
                LiveDataState.Success(LoadingState.IS_LOADED)
            )
        }
    }

    @Test
    fun `given retry when initialLoad in error then must return pagedList`() {
        runBlockingTest {
            viewModel = UserAlbumsViewModel(useCase)
            given(useCase.invoke("2529", 0)).willReturn(Outcome.Error(ErrorType.ERROR_UNKNOWN))
            val listValue = viewModel.albumList.getOrAwaitValue()
            val initialStateValue = viewModel.initialLoadingState?.getOrAwaitValue()
            assertEquals(listValue, listOf<AlbumItemData>())
            assertEquals(
                initialStateValue, LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.RECOVERABLE,
                        StringPresenter(R.string.error_fetch_data_title),
                        StringPresenter(R.string.error_fetch_data_description),
                        StringPresenter(R.string.retry)
                    )
                )
            )
            //retry it here
            given(
                useCase.invoke(
                    "2529",
                    0
                )
            ).willReturn(UseCaseModelMock.createSuccessAlbumOutCome())
            unconfinifyTestScope()
            viewModel.retry()
            assertEquals(listValue, LiveDataMock.createAlbumPaginedList())
        }
    }

    /**
     * Perform test on loadAfter method requires instrumented tests
     */
}