package com.gauvain.seigneur.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import kotlinx.coroutines.CoroutineScope

class UserAlbumsDataSourceFactory(
    val userName:String,
    val scope: CoroutineScope,
    val useCase: GetUserAlbumsUseCase
) : DataSource.Factory<Int, AlbumModel>() {

    val albumsDataSourceLiveData = MutableLiveData<UserAlbumsDataSource>()

    override fun create(): DataSource<Int, AlbumModel> {
        val dataSource = UserAlbumsDataSource(
            userName,
            scope,
            useCase
        )
        albumsDataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}