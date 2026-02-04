package com.example.translator.data.dto

import com.google.gson.annotations.SerializedName

data class Translation (

    @SerializedName("text" ) val text : String? = null,
    @SerializedName("note" ) val note : String? = null

)