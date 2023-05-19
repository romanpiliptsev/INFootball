package com.example.infootball.data.database.db_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_items")
data class MatchDbModel(
    @PrimaryKey
    val id: Int
)