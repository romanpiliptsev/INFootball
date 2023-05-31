package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetTeamResultsUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(teamId: Int) = repository.getFinishedMatchesOfTeam(teamId)
}