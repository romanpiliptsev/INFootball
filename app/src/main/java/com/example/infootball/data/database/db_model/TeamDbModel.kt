package com.example.infootball.data.database.db_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_items")
data class TeamDbModel(
    @PrimaryKey
    val id: Int,
//    val name: String
)