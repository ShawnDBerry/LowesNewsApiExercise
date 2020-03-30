package com.example.lowesnewsapiexercise.network

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class NewsRetrofitTest {

    @Test
    fun getTopNews() {
        runBlocking {
            val result = NewsRetrofit.getTopNews(1)
            assertEquals(result.status,"ok")
            assertNotNull(result.articles?.let{
                it[0]}
            )
        }
    }

    @Test
    fun searchNews() {
        runBlocking {
            val result = NewsRetrofit.searchNews("dogs",1)
            assertEquals(result.status,"ok")
            assertNotNull(result.articles?.let{
                it[0]
            })
        }
    }
}