package de.timbo.coracle.api.model

import com.google.gson.annotations.SerializedName

data class QuestionDto(

    @SerializedName("question_id")
    val questionId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("link")
    val link: String
)
