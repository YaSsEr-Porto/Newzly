package com.example.newzly.data.api

import com.example.newzly.data.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    fun getNews(
        @Query("country") country: String = "us",
        @Query("category") category: String = "general",
        @Query("apiKey") apiKey: String,
    ): Call<News>
}