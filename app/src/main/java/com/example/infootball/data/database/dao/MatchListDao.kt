package com.example.infootball.data.database.dao

import androidx.room.*
import com.example.infootball.data.database.db_model.MatchDbModel

@Dao
interface MatchListDao {

    @Query("SELECT * FROM match_items")
    fun getMatchList(): List<MatchDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMatchItem(matchItemDbModel: MatchDbModel)

    @Query("DELETE FROM match_items WHERE id=:matchItemId")
    suspend fun deleteMatchItem(matchItemId: Int)

    @Query("SELECT * FROM match_items WHERE id=:matchItemId LIMIT 1")
    suspend fun getMatchItem(matchItemId: Int): MatchDbModel?
}