package com.example.lowesnewsapiexercise.network

import com.example.lowesnewsapiexercise.model.NewsSource
import com.example.lowesnewsapiexercise.util.*
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET(GET_TOP_HEADLINES)
    suspend fun getTopNews(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_PAGE) pageNum: Int,
        @Query(QUERY_LANGUAGE) language: String
    ): NewsSource

    @GET(GET_EVERYTHING)
    suspend fun searchNews(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_Q) searchQuery: String,
        @Query(QUERY_PAGE) pageNum: Int
    ): NewsSource


}