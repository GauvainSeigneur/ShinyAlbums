package com.gauvain.seigneur.presentation.injection

import com.gauvain.seigneur.presentation.viewModel.UserAlbumsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        UserAlbumsViewModel(get())
    }
}