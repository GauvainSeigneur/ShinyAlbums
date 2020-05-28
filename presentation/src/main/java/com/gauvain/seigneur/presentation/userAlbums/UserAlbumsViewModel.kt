package com.gauvain.seigneur.presentation.userAlbums

import android.view.View
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.utils.event.Event
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

typealias displaysDetailsEventState = Event<LiveDataState<TransitionAlbumItemData>>

class UserAlbumsViewModel(
    private val useCase: GetUserAlbumsUseCase
) : ViewModel(), CoroutineScope {

    private val job = Job()
    private val albumDetailsList = mutableListOf<AlbumDetailsData>()
    val displaysDetailsEvent = MutableLiveData<displaysDetailsEventState>()
    private val config = PagedList.Config.Builder()
        .setPageSize(25)
        .setPrefetchDistance(25)
        .setEnablePlaceholders(false)
        .build()
    var initialLoadingState: LiveData<LiveDataState<LoadingState>>? = null
    var nextLoadingState: LiveData<LiveDataState<LoadingState>>? = null
    private val dataSourceFactory: UserAlbumsDataSourceFactory by lazy {
        UserAlbumsDataSourceFactory(
            "2529",
            viewModelScope,
            useCase
        )
    }
    val albumList: LiveData<PagedList<AlbumItemData>> by lazy {
        initialLoadingState = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it.initialLoadingState }
        nextLoadingState = Transformations.switchMap(dataSourceFactory.albumsDataSourceLiveData) { it.nextLoadingState }
        LivePagedListBuilder<Int, AlbumItemData>(dataSourceFactory.map { albumModel ->
            //populate list of albumModel if you have to
            albumDetailsList.add(albumModel.toAlbumDetailData())
            albumModel.toItemData()
        }, config).build()
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }

    fun retry() {
        dataSourceFactory.albumsDataSourceLiveData.value?.retryAllFailed()
    }

    fun getAlbumDetail(
        id: Long?, rootView: View, cardView: View, imageView: View
    ) {
        id?.let { idValue ->
            val item = albumDetailsList.firstOrNull { it.id == idValue }
            item?.let {
                displaysDetailsEvent.value = Event(
                    LiveDataState.Success(TransitionAlbumItemData(it, rootView, cardView, imageView))
                )
            }
        }
    }
}