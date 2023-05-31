package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import java.time.LocalDate
import javax.inject.Inject

class GetLeaguesOfMatchesUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(date: LocalDate) = repository.getLeaguesOfMatches(date)
}