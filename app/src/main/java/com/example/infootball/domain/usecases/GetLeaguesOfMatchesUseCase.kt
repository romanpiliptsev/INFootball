package com.example.infootball.domain.usecases

import android.app.Application
import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.repositories.MainRepository
import java.time.LocalDate

class GetLeaguesOfMatchesUseCase(application: Application) {
    private val repository: MainRepository = MainRepositoryImpl(application)

    suspend operator fun invoke(date: LocalDate) = repository.getLeaguesOfMatches(date)
}