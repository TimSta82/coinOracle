package de.timbo.coinOracle.api.model

import com.google.gson.annotations.SerializedName
import de.timbo.coinOracle.database.model.QuestionEntity

data class QuestionListDto(

    @SerializedName("items")
    val items: MutableList<QuestionDto>

) {
    fun toQuestionEntityList(): List<QuestionEntity> = items.map { item -> QuestionEntity(item) }
}
