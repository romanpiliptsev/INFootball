package com.example.infootball.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    //    private const val BASE_URL = "https://v3.football.api-sports.io/"
    private const val BASE_URL = "https://api.football-data.org/v4/"

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
//                builder.header("x-rapidapi-key", "d942b3f17607fb7a7b9452fb1d6874a7")
                builder.header("X-Auth-Token", "408ba40e9317468fa300038a5d9f3696")
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }.build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}