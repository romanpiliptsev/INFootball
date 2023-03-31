package com.example.infootball.domain

import com.example.infootball.data.network.model.LeagueResponseDto

interface MainRepository {

    suspend fun getLeagueResponseById(id: Int): LeagueResponseDto

    suspend fun getLeagueList(idList: List<Int>): List<LeagueResponseDto>
}