package com.example.lowesnewsapiexercise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lowesnewsapiexercise.model.Article
import com.example.lowesnewsapiexercise.network.NewsRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    val articlesLiveData = MutableLiveData<ArrayList<Article>>()
    val noResultsLiveData = MutableLiveData<Boolean>()

    fun getLatestNews(pageNum: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val articles = NewsRetrofit.getTopNews(pageNum).articles
            CoroutineScope(Dispatchers.Main).launch {
                updateLiveData(articles)
            }
        }
    }

    fun searchNews(query: String, pageNum: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val articles = NewsRetrofit.searchNews(query, pageNum).articles
            CoroutineScope(Dispatchers.Main).launch {
                updateLiveData(articles)
            }
        }
    }

    private fun updateLiveData(list: ArrayList<Article>?) {
        if (list != null&&list.size!=0) {
            articlesLiveData.value = (articlesLiveData.value?.apply {
                addAll(list)
            })?:list
        } else {
            noResults()
        }
    }

    private fun noResults() {
        noResultsLiveData.value=(noResultsLiveData.value)?.let {
            !it
        }?:true
    }

}