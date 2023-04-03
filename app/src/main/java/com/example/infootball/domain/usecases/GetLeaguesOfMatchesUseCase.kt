package com.example.infootball.domain.usecases

import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.MainRepository
import java.time.LocalDate

class GetLeaguesOfMatchesUseCase {
    private val repository: MainRepository = MainRepositoryImpl()

    suspend operator fun invoke(date: LocalDate) = repository.getLeaguesOfMatches(date)
}