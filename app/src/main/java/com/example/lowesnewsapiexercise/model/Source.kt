package com.example.lowesnewsapiexercise.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    @Expose
    val id: Any? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null
)