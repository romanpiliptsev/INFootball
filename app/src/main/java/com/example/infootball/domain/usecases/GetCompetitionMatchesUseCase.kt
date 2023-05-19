package com.example.infootball.domain.usecases

import android.app.Application
import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.repositories.MainRepository

class GetCompetitionMatchesUseCase(application: Application) {
    private val repository: MainRepository = MainRepositoryImpl(application)

    suspend operator fun invoke(leagueCode: String, season: String, isFinished: Boolean) =
        repository.getCompetitionMatches(leagueCode, season, isFinished)
}