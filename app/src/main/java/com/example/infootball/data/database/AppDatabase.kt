package com.example.infootball.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.infootball.data.database.dao.MatchListDao
import com.example.infootball.data.database.dao.TeamListDao
import com.example.infootball.data.database.db_model.MatchDbModel
import com.example.infootball.data.database.db_model.TeamDbModel

@Database(entities = [MatchDbModel::class, TeamDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun teamListDao(): TeamListDao
    abstract fun matchListDao(): MatchListDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "app_database.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}