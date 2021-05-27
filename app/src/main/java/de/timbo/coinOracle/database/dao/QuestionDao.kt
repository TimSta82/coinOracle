package de.timbo.coinOracle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import de.timbo.coinOracle.database.model.QuestionEntity

@Dao
abstract class QuestionDao : BaseDao<QuestionEntity>() {

    @Query("SELECT * FROM QuestionEntity")
    abstract fun watchAll(): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM QuestionEntity WHERE question_id = :questionId")
    abstract fun watchQuestionById(questionId: String): LiveData<QuestionEntity>

    @Query("DELETE FROM QuestionEntity")
    abstract suspend fun removeAll()
}
