package com.gauvain.seigneur.dataadapter.injection

import android.util.Log
import com.gauvain.seigneur.dataadapter.service.DeezerService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteDataSourceModule = module {
    factory<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.d("DEEZER_SERVICE", it)
        }
        ).setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    //set as factory in order to update GetTokenAdapter.constToken value when it is done
    factory {
        OkHttpClient.Builder()
            .addInterceptor(get())
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(getProperty("server_url") as String)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(DeezerService::class.java) }
}