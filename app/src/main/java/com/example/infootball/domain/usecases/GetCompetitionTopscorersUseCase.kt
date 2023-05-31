package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetCompetitionTopscorersUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(leagueCode: String, limit: Int, season: String) =
        repository.getCompetitionTopscorers(leagueCode, limit, season)
}