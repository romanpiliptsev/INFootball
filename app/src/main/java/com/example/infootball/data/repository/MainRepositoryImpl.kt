package com.example.infootball.data.repository

import com.example.infootball.data.network.ApiFactory
import com.example.infootball.data.network.model.MatchDto
import com.example.infootball.domain.MainRepository
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import java.time.LocalDate

class MainRepositoryImpl : MainRepository {

    private val apiService = ApiFactory.apiService

    override suspend fun getMatchesOfLeagueDay(
        competition: String,
        date: String
    ): ArrayList<MatchDto> {
        return apiService.getMatchesOfLeagueDays(
            competition,
            date,
            LocalDate.parse(date).plusDays(1).toString()
        ).matches ?: throw Exception()
    }

    override suspend fun getLeaguesOfMatches(date: LocalDate): List<LeagueOfMatchesEntity> {
        val matchesMainResponseDto =
            apiService.getMatchesForLeaguesOfMatches(date.toString(), date.plusDays(1).toString())

        val result = arrayListOf<LeagueOfMatchesEntity>()
        matchesMainResponseDto.resultSet?.competitions?.split(',')?.forEach { code ->
            val matches = matchesMainResponseDto.matches?.filter { it.competition?.code == code }
            if (matches?.isNotEmpty() == true) {
                result.add(
                    LeagueOfMatchesEntity(
                        matches[0].competition?.name ?: throw Exception(),
                        matches[0].competition?.code ?: throw Exception(),
                        matches[0].area?.name ?: throw Exception(),
                        matches[0].competition?.emblem ?: throw Exception(),
                        matches[0].area?.flag ?: throw Exception(),
                        matches.size,
                        matches.filter { it.status == "IN_PLAY" }.size
                    )
                )
            }
        }
        return result
    }
}