package com.example.lowesnewsapiexercise.viewmodel

import com.example.lowesnewsapiexercise.viewmodel.NewsViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class NewsViewModelTest {

    var newsViewModel : NewsViewModel? = null

    @Test
    fun getLatestNews(pageNum: Int) {
        runBlocking {
            newsViewModel?.getLatestNews(pageNum)
            assertNotEquals(newsViewModel?.articlesLiveData?.value?.size,0)
        }
    }

    @Test
    fun searchNews() {
        runBlocking {
            newsViewModel?.searchNews("a;lsdkfjadl;fjk",1)
            assertEquals(null, newsViewModel?.articlesLiveData?.value)
            newsViewModel?.searchNews("dogs are cool",1)
            assertNotEquals(0, newsViewModel?.articlesLiveData?.value?.size)
        }
    }
}