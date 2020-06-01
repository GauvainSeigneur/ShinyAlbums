package com.gauvain.seigneur.presentation.userAlbums

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.gauvain.seigneur.domain.model.AlbumModel
import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import kotlinx.coroutines.CoroutineScope

class UserAlbumsDataSourceFactory(
    val userName: String,
    val useCase: GetUserAlbumsUseCase,
    val scope: CoroutineScope
) : DataSource.Factory<Int, AlbumModel>() {

    val albumsDataSourceLiveData = MutableLiveData<UserAlbumsDataSource>()

    override fun create(): DataSource<Int, AlbumModel> {
        val dataSource = UserAlbumsDataSource(
            userName,
            useCase,
            scope
        )
        albumsDataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}