package com.example.infootball.domain.usecases

import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.MainRepository

class GetLeagueResponseByIdUseCase {
    private val repository: MainRepository = MainRepositoryImpl()

    suspend operator fun invoke(id: Int) = repository.getLeagueResponseById(id)
}