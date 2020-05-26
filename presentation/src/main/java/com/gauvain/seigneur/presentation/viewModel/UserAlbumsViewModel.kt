package com.gauvain.seigneur.presentation.viewModel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.model.ErrorData
import com.gauvain.seigneur.presentation.model.LoadingState
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

    var albumList: LiveData<PagedList<AlbumModel>>? = null
    var initialLoadingState: LiveData<LoadingState>? = null
    var nextLoadingState: LiveData<LoadingState>? = null
    var initialErrorData: LiveData<ErrorData>? = null
    var nextErrorData: LiveData<ErrorData>? = null

    fun fetchAlbums() {
        val dataSourceFactory = UserAlbumsDataSourceFactory(
            "2529",
            viewModelScope,
            useCase
        )

        albumList = LivePagedListBuilder<Int, AlbumModel>(dataSourceFactory, config).build()

        initialLoadingState = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it.initialLoadingData }
        nextLoadingState = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it.nextLoadingData }
        initialErrorData = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it.initialErrorData }
        nextErrorData = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it.nextErrorData }
    }

}