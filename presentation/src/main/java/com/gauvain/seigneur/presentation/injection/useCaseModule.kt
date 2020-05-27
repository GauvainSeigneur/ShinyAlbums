package com.gauvain.seigneur.presentation.injection

import com.gauvain.seigneur.domain.useCase.GetUserAlbumsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetUserAlbumsUseCase.create(get()) }
}