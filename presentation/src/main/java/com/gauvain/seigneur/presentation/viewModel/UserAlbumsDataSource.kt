package com.gauvain.seigneur.presentation.viewModel

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserAlbumsDataSource(
    private val userName: String,
    private val scope: CoroutineScope,
    private val useCase: GetUserAlbumsUseCase
) : PageKeyedDataSource<Int, AlbumModel>() {

    private var isLastPage = false
    val initialLoadingState = MutableLiveData<LiveDataState<LoadingState>>()
    val nextLoadingState = MutableLiveData<LiveDataState<LoadingState>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AlbumModel>
    ) {
        scope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                useCase.invoke(userName, 0)
            }
            initialLoadingState.value = LiveDataState.Success(LoadingState.IS_LOADING)
            when (result) {
                is Outcome.Success -> {
                    initialLoadingState.value = LiveDataState.Success(LoadingState.IS_LOADED)
                    isLastPage = result.data.next == null
                    callback.onResult(result.data.albums, null, result.data.next)
                }
                is Outcome.Error -> {
                    initialLoadingState.value = LiveDataState.Error(
                        ErrorData(
                            ErrorDataType.RECOVERABLE,
                            StringPresenter(R.string.error_fetch_data_title),
                            StringPresenter(R.string.error_fetch_data_description),
                            StringPresenter(R.string.retry)
                        )
                    )
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AlbumModel>) {
        if (!isLastPage) {
            scope.launch(Dispatchers.Main) {
                nextLoadingState.value = LiveDataState.Success(LoadingState.IS_LOADING)
                val result = withContext(Dispatchers.IO) {
                    useCase.invoke(userName, params.key)
                }
                nextLoadingState.value = LiveDataState.Success(LoadingState.IS_LOADED)
                when (result) {
                    is Outcome.Success -> {
                        isLastPage = result.data.next == null
                        callback.onResult(result.data.albums, result.data.next)
                    }
                    is Outcome.Error -> {
                        nextLoadingState.value = LiveDataState.Error(
                            ErrorData(
                                ErrorDataType.RECOVERABLE,
                                StringPresenter(R.string.error_fetch_data_title),
                                StringPresenter(R.string.error_fetch_data_description),
                                StringPresenter(R.string.retry)
                            )
                        )
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AlbumModel>) {
    }
}