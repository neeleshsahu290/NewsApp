package com.example.newsapp.network

import com.example.newsapp.models.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("apiKey") key: String,@Query("country")country:String ): Response<News>

    @GET("everything")
    suspend fun getEverything(@Query("apiKey") key: String,
                              @Query("q")itemQuery:String,
                              @Query("sortBy")shortBy:String?
                              ): Response<News>
}

//https://newsapi.org/v2/everything?q=tesla&from=2022-08-18&sortBy=publishedAt&apiKey=API_KEY