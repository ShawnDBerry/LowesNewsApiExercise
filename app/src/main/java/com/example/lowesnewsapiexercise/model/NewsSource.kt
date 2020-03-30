package com.example.lowesnewsapiexercise.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewsSource(
    @SerializedName("status")
    @Expose
    val status: String? = null,
    @SerializedName("totalResults")
    @Expose
    val totalResults: Integer? = null,
    @SerializedName("articles")
    @Expose
    val articles: ArrayList<Article>? = null
)