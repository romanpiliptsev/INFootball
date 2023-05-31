package com.example.infootball.domain.usecases

import com.example.infootball.domain.repositories.MainRepository
import javax.inject.Inject

class AddFavoriteMatchUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(matchId: Int) =
        repository.addMatchItem(matchId)
}