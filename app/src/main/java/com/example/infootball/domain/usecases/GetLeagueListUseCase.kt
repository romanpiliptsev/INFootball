package com.example.infootball.domain.usecases

import com.example.infootball.data.repository.MainRepositoryImpl
import com.example.infootball.domain.MainRepository

class GetLeagueListUseCase {
    private val repository: MainRepository = MainRepositoryImpl()

    suspend operator fun invoke(idList: List<Int>) = repository.getLeagueList(idList)
}