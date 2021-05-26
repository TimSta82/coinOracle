package de.bornholdtlee.defaultprojectkotlin.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.bornholdtlee.defaultprojectkotlin.api.model.QuestionDto

@Entity
data class QuestionEntity(

    @PrimaryKey
    @ColumnInfo(name = "question_id")
    var questionId: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "link")
    var link: String
) {
    constructor(dto: QuestionDto) : this(
        questionId = dto.questionId,
        title = dto.title,
        link = dto.link
    )
}
