package com.gauvain.seigneur.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.model.ErrorData
import com.gauvain.seigneur.presentation.model.ErrorDataType
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
    val initialLoadingData = MutableLiveData<LoadingState>()
    val initialErrorData = MutableLiveData<ErrorData>()

    val nextLoadingData = MutableLiveData<LoadingState>()
    val nextErrorData = MutableLiveData<ErrorData>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, AlbumModel>
    ) {
        scope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                useCase.invoke(userName, 0)
            }
            initialLoadingData.value = LoadingState.IS_LOADING
            when (result) {
                is Outcome.Success -> {
                    initialLoadingData.value = LoadingState.IS_LOADED
                    isLastPage = result.data.next == null
                    callback.onResult(result.data.albums, null, result.data.next)
                }
                is Outcome.Error -> {
                    initialErrorData.value = ErrorData(
                        ErrorDataType.RECOVERABLE,
                        StringPresenter(R.string.error_fetch_data_title),
                        StringPresenter(R.string.error_fetch_data_description),
                        StringPresenter(R.string.retry)
                    )
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, AlbumModel>) {
        if (!isLastPage) {
            scope.launch(Dispatchers.Main) {
                nextLoadingData.value = LoadingState.IS_LOADING
                val result = withContext(Dispatchers.IO) {
                    useCase.invoke(userName, params.key)
                }
                //nextLoadingData.value = LoadingState.IS_LOADED
                when (result) {
                    is Outcome.Success -> {
                        isLastPage = result.data.next == null
                        callback.onResult(result.data.albums, result.data.next)
                    }
                    is Outcome.Error -> {
                        nextErrorData.value = ErrorData(
                            ErrorDataType.RECOVERABLE,
                            StringPresenter(R.string.error_fetch_data_title),
                            StringPresenter(R.string.error_fetch_data_description),
                            StringPresenter(R.string.retry)
                        )
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, AlbumModel>) {
    }
}