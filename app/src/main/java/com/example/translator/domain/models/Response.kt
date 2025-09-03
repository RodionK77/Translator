package com.example.translator.domain.models

import com.google.gson.annotations.SerializedName

data class Response (

    @SerializedName("id"       ) val id       : Int?                = null,
    @SerializedName("text"     ) val text     : String?             = null,
    @SerializedName("meanings" ) val meanings : ArrayList<Meanings> = arrayListOf()

)