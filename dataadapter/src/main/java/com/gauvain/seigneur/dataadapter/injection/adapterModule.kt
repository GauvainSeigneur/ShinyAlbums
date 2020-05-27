package com.gauvain.seigneur.dataadapter.injection

import com.gauvain.seigneur.dataadapter.adapter.GetUserAlbumsAdapter
import com.gauvain.seigneur.domain.provider.*
import org.koin.dsl.module

val adapterModule = module {
    single<GetUserAlbumsProvider> {
        GetUserAlbumsAdapter(get())
    }
}