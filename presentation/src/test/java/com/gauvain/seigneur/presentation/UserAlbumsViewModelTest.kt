package com.gauvain.seigneur.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.utils.MainCoroutineRule
import com.gauvain.seigneur.presentation.utils.getOrAwaitValue
import com.gauvain.seigneur.presentation.userAlbums.UserAlbumsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
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

    fun test() {
        //todo - mockDataSource and mock useCase calback
    }


    fun <T> List<T>.asPagedList(page: Int, config: PagedList.Config? = null): PagedList<T>? {
        val defaultConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(25)
            .setPrefetchDistance(25)
            .build()
        return LivePagedListBuilder<Int, T>(
            createMockDataSourceFactory(this, page),
            config ?: defaultConfig
        ).build().getOrAwaitValue()
    }


    private fun <T> createMockDataSourceFactory(itemList: List<T>, page:Int): DataSource
    .Factory<Int, T> =
        object : DataSource.Factory<Int, T>() {
            override fun create(): DataSource<Int, T> = MockPageKeyedDataSource(page, itemList)
        }

    class MockPageKeyedDataSource<Int, T>(private val page: Int,
                                      private val itemList: List<T>) :
        PageKeyedDataSource<Int, T>() {

        override fun isInvalid(): Boolean = false

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, T>
        ) {
            callback.onResult(itemList, null, page)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
            callback.onResult(itemList, page)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

        }
    }
}
