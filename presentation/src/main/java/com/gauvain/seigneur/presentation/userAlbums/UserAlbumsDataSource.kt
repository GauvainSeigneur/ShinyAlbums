package com.gauvain.seigneur.presentation.userAlbums

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.model.ErrorData
import com.gauvain.seigneur.presentation.model.ErrorDataType
import com.gauvain.seigneur.presentation.model.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.utils.StringPresenter
import kotlinx.coroutines.*

class UserAlbumsDataSource(
    private val userName: String,
    private val useCase: GetUserAlbumsUseCase,
    val scope: CoroutineScope
) : PageKeyedDataSource<Int, AlbumModel>() {

    private var retry: (() -> Any)? = null
    private var isLastPage = false
    val initialLoadingState = MutableLiveData<LiveDataState<LoadingState>>()
    val nextLoadingState = MutableLiveData<LiveDataState<LoadingState>>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AlbumModel>
    ) {
        scope.launch(Dispatchers.IO) {
            initialLoadingState.postValue(LiveDataState.Success(LoadingState.IS_LOADING))
            val result = useCase.invoke(userName, 0)
            when (result) {
                is Outcome.Success -> {
                    // clear retry since last request succeeded
                    retry = null
                    initialLoadingState.postValue(LiveDataState.Success(LoadingState.IS_LOADED))
                    isLastPage = result.data.next == null
                    callback.onResult(result.data.albums, null, result.data.next)
                }
                is Outcome.Error -> {
                    retry = {
                        loadInitial(params, callback)
                    }
                    initialLoadingState.postValue(
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AlbumModel>) {
        scope.launch(Dispatchers.IO) {
            if (!isLastPage) {
                nextLoadingState.postValue(LiveDataState.Success(LoadingState.IS_LOADING))
                val result =
                    useCase.invoke(userName, params.key)
                nextLoadingState.postValue(LiveDataState.Success(LoadingState.IS_LOADED))
                when (result) {
                    is Outcome.Success -> {
                        // clear retry since last request succeeded
                        retry = null
                        isLastPage = result.data.next == null
                        callback.onResult(result.data.albums, result.data.next)
                    }
                    is Outcome.Error -> {
                        retry = {
                            loadAfter(params, callback)
                        }
                        nextLoadingState.postValue(
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
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AlbumModel>) {
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}