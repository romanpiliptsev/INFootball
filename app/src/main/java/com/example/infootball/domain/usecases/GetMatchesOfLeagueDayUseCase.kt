package com.example.infootball.domain.usecases

import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.MainRepository
import java.time.LocalDate

class GetMatchesOfLeagueDayUseCase {
    private val repository: MainRepository = MainRepositoryImpl()

    suspend operator fun invoke(competitions: String, date: String) =
        repository.getMatchesOfLeagueDay(competitions, date)
}