package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class GetLiveMatchesUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke() = repository.getLiveMatches()
}