package com.gauvain.seigneur.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.mock.LiveDataModelMock
import com.gauvain.seigneur.presentation.mock.UseCaseModelMock
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.utils.MainCoroutineRule
import com.gauvain.seigneur.presentation.utils.getOrAwaitValue
import com.gauvain.seigneur.presentation.viewModel.UserAlbumsViewModel
import com.nhaarman.mockitokotlin2.given
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
    @InjectMocks
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
    fun
        given_usecase_return_paginedAlbumModel_when_fetch_albums_then_liveData_must_return_data() {
        runBlockingTest {
            given(useCase.invoke("userid", 0)).willReturn(
                UseCaseModelMock.createSuccessPaginedAlbumUseCaseOutCome()
            )
            val value = viewModel.albumList.getOrAwaitValue()
            //mainCoroutineRule.advanceUntilIdle()
            assertEquals(
                value,
                //mockPagedList(LiveDataModelMock.createAlbumsPaginedLiveData())
                LiveDataModelMock.createAlbumsPaginedLiveData()

            )
        }
    }

    fun <T> mockPagedList(list: List<T>): PagedList<T> {
        val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
        Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
            val index = invocation.arguments.first() as Int
            list[index]
        }
        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagedList
    }

}