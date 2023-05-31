package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetMatchesOfLeagueDayUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(competitions: String, date: String) =
        repository.getMatchesOfLeagueDay(competitions, date)
}