package com.gauvain.seigneur.shinyalbums

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.gauvain.seigneur.dataadapter.injection.adapterModule
import com.gauvain.seigneur.dataadapter.injection.remoteDataSourceModule
import com.gauvain.seigneur.presentation.injection.useCaseModule
import com.gauvain.seigneur.presentation.injection.viewModelModule
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShinyAlbumsApp : Application(), ImageLoaderFactory {

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

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this@ShinyAlbumsApp).apply {
            componentRegistry {
                add(SvgDecoder(this@ShinyAlbumsApp))
            }
        }
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this@ShinyAlbumsApp))
                    .build()
            }
            .build()
    }
}