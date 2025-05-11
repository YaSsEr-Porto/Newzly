package com.example.newzly.data.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newzly.data.api.NewsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val _news = MutableLiveData<News>()
    val news: LiveData<News> get() = _news

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadNews() {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val newsService = retrofit.create(NewsService::class.java)
        newsService.getNews(apiKey = "API_KEY").enqueue(object : Callback<News> {

            override fun onResponse(call: Call<News?>, response: Response<News?>) {

                if (response.isSuccessful && response.body() != null) {
                    _news.value = response.body()
                } else {
                    _error.value = "Failed to load news ${response.message()}"
                }
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
                _error.value = "Failed to load news: ${t.message}"

            }
        })
    }
}