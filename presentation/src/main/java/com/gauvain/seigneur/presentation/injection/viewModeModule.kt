package com.gauvain.seigneur.presentation.injection

import com.gauvain.seigneur.presentation.albumDetails.AlbumDetailsViewModel
import com.gauvain.seigneur.presentation.model.AlbumDetailsData
import com.gauvain.seigneur.presentation.userAlbums.UserAlbumsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        UserAlbumsViewModel(get())
    }
    viewModel { (detailsData: AlbumDetailsData) ->
        AlbumDetailsViewModel(detailsData, get())
    }
}