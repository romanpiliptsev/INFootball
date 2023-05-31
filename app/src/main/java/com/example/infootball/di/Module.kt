package com.example.infootball.di

import android.app.Application
import com.example.infootball.data.database.AppDatabase
import com.example.infootball.data.database.dao.MatchListDao
import com.example.infootball.data.database.dao.TeamListDao
import com.example.infootball.data.network.ApiFactory
import com.example.infootball.data.network.ApiService
import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.repositories.MainRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface Module {

    @Binds
    @ApplicationScope
    fun bindServiceRepository(impl: MainRepositoryImpl): MainRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @Provides
        @ApplicationScope
        fun provideAppDatabase(application: Application): AppDatabase {
            return AppDatabase.getInstance(application)
        }

        @Provides
        @ApplicationScope
        fun provideMatchListDao(appDatabase: AppDatabase): MatchListDao {
            return appDatabase.matchListDao()
        }

        @Provides
        @ApplicationScope
        fun provideTeamListDao(appDatabase: AppDatabase): TeamListDao {
            return appDatabase.teamListDao()
        }
    }
}