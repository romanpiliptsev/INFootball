package com.example.infootball.domain

import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.data.network.model.MatchesMainResponseDto
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import java.time.LocalDate

interface MainRepository {

    suspend fun getMatchesOfLeagueDay(competition: String, date: String): ArrayList<MatchDto>

    suspend fun getLeaguesOfMatches(date: LocalDate): List<LeagueOfMatchesEntity>
}