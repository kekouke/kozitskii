package com.kekouke.movies.data.datasource

import com.kekouke.movies.data.KinopoiskApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RemoteDataSource {


    val apiService = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create<KinopoiskApiService>()
}