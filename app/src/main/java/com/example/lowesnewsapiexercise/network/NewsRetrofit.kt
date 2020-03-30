package com.example.lowesnewsapiexercise.network

import com.example.lowesnewsapiexercise.model.NewsSource
import com.example.lowesnewsapiexercise.util.API_KEY
import com.example.lowesnewsapiexercise.util.BASE_URL
import com.example.lowesnewsapiexercise.util.LANGUAGE_EN
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsRetrofit {
    private val retrofit: Retrofit by lazy{
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val newsService: NewsService by lazy{
        retrofit.create(NewsService::class.java)
    }

    suspend fun getTopNews(pageNum: Int): NewsSource{
        return newsService.getTopNews(API_KEY, pageNum, LANGUAGE_EN)
    }

    suspend fun searchNews(searchQuery: String, pageNum: Int) : NewsSource {
        return newsService.searchNews(API_KEY,searchQuery,pageNum)
    }
}