package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetMatchH2HUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(matchId: Int) = repository.getMatchH2H(matchId)
}