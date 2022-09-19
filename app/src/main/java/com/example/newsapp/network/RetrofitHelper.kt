package com.example.newsapp.network

import com.example.newsapp.utility.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    //private val Baseurl="http://192.168.100.117/"
    var gson = GsonBuilder()
        .setLenient()
        .create()

    var httpClient = OkHttpClient.Builder()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}