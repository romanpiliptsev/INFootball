package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetCompetitionStandingsUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(code: String, season: String) =
        repository.getCompetitionStandings(code, season)
}