package com.gauvain.seigneur.shinyalbums

import android.app.Application
import com.gauvain.seigneur.dataadapter.injection.adapterModule
import com.gauvain.seigneur.dataadapter.injection.remoteDataSourceModule
import com.gauvain.seigneur.presentation.injection.useCaseModule
import com.gauvain.seigneur.presentation.injection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShinyAlbumsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ShinyAlbumsApp)
            androidFileProperties()
            modules(
                listOf(
                    remoteDataSourceModule,
                    adapterModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}