package com.example.lowesnewsapiexercise.util

fun String.parseDate(): String{
    val parsedAroundT = this.split("T")
    return parsedAroundT[0]
}