package com.example.translator.data

import com.example.translator.domain.models.Meanings
import com.example.translator.domain.models.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MeaningsAPI {

    @GET("/api/public/v1/words/search")
    suspend fun getMeanings(@Query("search") search: String = "",
                                @Query("page") page: Int = 1,
                                @Query("pageSize") limit: Int = 1 ) : List<Response>
}