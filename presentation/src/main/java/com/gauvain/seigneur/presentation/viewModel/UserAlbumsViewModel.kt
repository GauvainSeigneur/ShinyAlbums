package com.gauvain.seigneur.presentation.viewModel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.model.AlbumItemData
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.toItemData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UserAlbumsViewModel(
    private val useCase: GetUserAlbumsUseCase
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }

    private val config = PagedList.Config.Builder()
        .setPageSize(25)
        .setPrefetchDistance(25)
        .setEnablePlaceholders(false)
        .build()


    var initialLoadingState: LiveData<LiveDataState<LoadingState>>? = null
    var nextLoadingState: LiveData<LiveDataState<LoadingState>>? = null
    val dataSourceFactory: UserAlbumsDataSourceFactory by lazy {
        UserAlbumsDataSourceFactory(
            "2529",
            viewModelScope,
            useCase
        )
    }

    val albumList: LiveData<PagedList<AlbumItemData>> by lazy {
        initialLoadingState = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it
            .initialLoadingState }
        nextLoadingState = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it
            .nextLoadingState }

        LivePagedListBuilder<Int, AlbumItemData>(dataSourceFactory.map { albumModel ->
            //populate list of albumModel if you have to
            albumModel.toItemData()
        }, config).build()
    }



    fun retry() {
        dataSourceFactory.albumsDataSourceLiveData.value?.retryAllFailed()
    }

}