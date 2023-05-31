package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetCompetitionUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(competitionCode: String) =
        repository.getCompetition(competitionCode)
}