package com.example.infootball.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infootball.data.database.db_model.TeamDbModel

@Dao
interface TeamListDao {

    @Query("SELECT * FROM team_items")
    fun getTeamList(): List<TeamDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeamItem(teamItemDbModel: TeamDbModel)

    @Query("DELETE FROM team_items WHERE id=:teamItemId")
    suspend fun deleteTeamItem(teamItemId: Int)

    @Query("SELECT * FROM team_items WHERE id=:teamItemId LIMIT 1")
    suspend fun getTeamItem(teamItemId: Int): TeamDbModel?
}