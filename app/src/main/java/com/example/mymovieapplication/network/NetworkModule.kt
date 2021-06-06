package ru.surfstudio.android.easyadapter.sample.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.android.easyadapter.sample.ui.utils.Constants

object NetworkModule {
    fun provideMovieApiService(): MovieApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}