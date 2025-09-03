package com.example.translator.domain.models

import com.google.gson.annotations.SerializedName

data class Meanings (

    @SerializedName("id"               ) val id               : Int?         = null,
    @SerializedName("partOfSpeechCode" ) val partOfSpeechCode : String?      = null,
    @SerializedName("translation"      ) val translation      : Translation? = Translation(),
    @SerializedName("previewUrl"       ) val previewUrl       : String?      = null,
    @SerializedName("imageUrl"         ) val imageUrl         : String?      = null,
    @SerializedName("transcription"    ) val transcription    : String?      = null,
    @SerializedName("soundUrl"         ) val soundUrl         : String?      = null

)