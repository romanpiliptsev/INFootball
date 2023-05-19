package com.example.infootball.domain.repositories

import com.example.infootball.data.network.model.*
import com.example.infootball.domain.entities.ExtendedTeamEntity
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import java.time.LocalDate

interface MainRepository {

    suspend fun getMatchesOfLeagueDay(competition: String, date: String): ArrayList<MatchDto>

    suspend fun getLeaguesOfMatches(date: LocalDate): List<LeagueOfMatchesEntity>

    suspend fun getMatch(matchId: Int): MatchDto

    suspend fun getMatchH2H(matchId: Int): H2HDto

    suspend fun getCompetitionStandings(code: String, season: String): StandingsResponseDto

    suspend fun getTeamById(teamId: Int): ExtendedTeamEntity

    suspend fun getFinishedMatchesOfTeam(teamId: Int): ArrayList<MatchDto>

    suspend fun getNotFinishedMatchesOfTeam(teamId: Int): ArrayList<MatchDto>

    suspend fun getLiveMatches(): ArrayList<MatchDto>

    suspend fun getCompetitions(): ArrayList<CompetitionWithAreaDto>

    suspend fun getCompetition(competitionCode: String): CompetitionWithAreaDto

    suspend fun getCompetitionMatches(
        leagueCode: String,
        season: String,
        isFinished: Boolean
    ): ArrayList<MatchDto>

    suspend fun getCompetitionTopscorers(
        leagueCode: String,
        limit: Int,
        season: String
    ): ArrayList<TopscorerDto>

    // ---------------------------------------------------------------------------------------------

    suspend fun isTeamFavorite(teamId: Int): Boolean

    suspend fun addTeamItem(teamId: Int)

    suspend fun deleteTeamItem(teamId: Int)

    suspend fun isMatchFavorite(matchId: Int): Boolean

    suspend fun addMatchItem(matchId: Int)

    suspend fun deleteMatchItem(matchId: Int)

    suspend fun getAllFavoriteMatches(): List<MatchDto>
}