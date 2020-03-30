package com.example.lowesnewsapiexercise.adapter

import com.example.lowesnewsapiexercise.util.parseDate
import org.junit.Assert.*
import org.junit.Test

class NewsRVAdapterTest{

    @Test
    fun testAdapterDateParseMethod(){
        assertEquals("1:2:3","1:2:3T321".parseDate())
        assertEquals("1:2:3","1:2:3".parseDate())
        assertEquals("","".parseDate())
        assertEquals("","".parseDate())
    }
}