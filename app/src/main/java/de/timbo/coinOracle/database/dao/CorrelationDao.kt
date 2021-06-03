package de.timbo.coinOracle.database.dao

import androidx.room.Dao
import de.timbo.coinOracle.database.model.CorrelationEntity

@Dao
abstract class CorrelationDao : BaseDao<CorrelationEntity>()
