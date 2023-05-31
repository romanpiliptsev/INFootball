package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetCompetitionMatchesUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(leagueCode: String, season: String, isFinished: Boolean) =
        repository.getCompetitionMatches(leagueCode, season, isFinished)
}