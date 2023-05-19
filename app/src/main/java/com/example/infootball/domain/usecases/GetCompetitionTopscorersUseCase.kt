package com.example.infootball.domain.usecases

import android.app.Application
import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.repositories.MainRepository

class GetCompetitionTopscorersUseCase(application: Application) {
    private val repository: MainRepository = MainRepositoryImpl(application)

    suspend operator fun invoke(leagueCode: String, limit: Int, season: String) =
        repository.getCompetitionTopscorers(leagueCode, limit, season)
}