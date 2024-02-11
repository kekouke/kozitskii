package com.kekouke.movies.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MovieRemoteDataSource {


    val apiService = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create<KinopoiskApiService>()
}