package com.example.infootball.data.repository

import com.example.infootball.data.database.dao.MatchListDao
import com.example.infootball.data.database.dao.TeamListDao
import com.example.infootball.data.database.db_model.MatchDbModel
import com.example.infootball.data.database.db_model.TeamDbModel
import com.example.infootball.data.network.ApiService
import com.example.infootball.data.network.model.*
import com.example.infootball.domain.entities.ExtendedTeamEntity
import com.example.infootball.domain.entities.LeagueOfMatchesEntity
import com.example.infootball.domain.repositories.MainRepository
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val matchListDao: MatchListDao,
    private val teamListDao: TeamListDao
) : MainRepository {

    override suspend fun getMatchesOfLeagueDay(
        competition: String,
        date: String
    ): ArrayList<MatchDto> {
        return apiService.getMatchesOfLeagueDays(
            competition,
            date,
            LocalDate.parse(date).plusDays(1).toString()
        ).matches ?: throw RuntimeException()
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
                        matches.filter { it.status == "IN_PLAY" || it.status == "PAUSED" }.size
                    )
                )
            }
        }
        return result
    }

    override suspend fun getMatch(matchId: Int): MatchDto {
        return apiService.getMatch(matchId)
    }

    override suspend fun getMatchH2H(matchId: Int): H2HDto {
        return apiService.getMatchH2H(matchId)
    }

    override suspend fun getCompetitionStandings(
        code: String,
        season: String
    ): StandingsResponseDto {
        return apiService.getCompetitionStandings(code, season)
    }

    override suspend fun getTeamById(teamId: Int): ExtendedTeamEntity {
        return with(apiService.getTeamById(teamId)) {
            ExtendedTeamEntity(
                area,
                id,
                name,
                shortName,
                tla,
                crest,
                address,
                website,
                founded,
                clubColors,
                venue,
                runningCompetitions.find { it.type == "LEAGUE" }?.code ?: "",
                coach,
                squad
            )
        }
    }

    override suspend fun getFinishedMatchesOfTeam(teamId: Int): ArrayList<MatchDto> {
        return apiService.getFinishedMatchesOfTeam(teamId).matches?.reversed()
            ?.let { ArrayList(it) }
            ?: throw RuntimeException()
    }

    override suspend fun getNotFinishedMatchesOfTeam(teamId: Int): ArrayList<MatchDto> {
        return apiService.getNotFinishedMatchesOfTeam(teamId).matches ?: throw RuntimeException()
    }

    override suspend fun getLiveMatches(): ArrayList<MatchDto> {
        return apiService.getLiveMatches().matches ?: throw RuntimeException()
    }

    override suspend fun getCompetitions(): ArrayList<CompetitionWithAreaDto> {
        return apiService.getCompetitions().competitions
            ?.filter { it.code != "EC" && it.code != "WC" }
            ?.let { ArrayList(it) }
            ?: throw RuntimeException()
    }

    override suspend fun getCompetition(competitionCode: String): CompetitionWithAreaDto {
        return apiService.getCompetition(competitionCode)
    }

    override suspend fun getCompetitionMatches(
        leagueCode: String,
        season: String,
        isFinished: Boolean
    ): ArrayList<MatchDto> {
        val status = when (isFinished) {
            true -> listOf(
                ApiService.STATUS_FINISHED, ApiService.STATUS_LIVE, ApiService.STATUS_PAUSED,
                ApiService.STATUS_IN_PLAY, ApiService.STATUS_SUSPENDED
            ).joinToString(separator = ",")
            false -> listOf(
                ApiService.STATUS_POSTPONED, ApiService.STATUS_SCHEDULED,
                ApiService.STATUS_TIMED
            ).joinToString(separator = ",")
        }
        return (
                if (isFinished)
                    apiService.getCompetitionMatches(
                        leagueCode,
                        season,
                        status
                    ).matches?.reversed()?.let { ArrayList(it) }
                else
                    apiService.getCompetitionMatches(leagueCode, season, status).matches
                ) ?: throw RuntimeException()
    }

    override suspend fun getCompetitionTopscorers(
        leagueCode: String,
        limit: Int,
        season: String
    ): ArrayList<TopscorerDto> {
        return apiService.getCompetitionTopscorers(leagueCode, limit, season).scorers
    }

    // ---------------------------------------------------------------------------------------------

    override suspend fun isTeamFavorite(teamId: Int): Boolean {
        return teamListDao.getTeamItem(teamId) != null
    }

    override suspend fun addTeamItem(teamId: Int) {
        teamListDao.addTeamItem(
            TeamDbModel(teamId)
        )
    }

    override suspend fun deleteTeamItem(teamId: Int) {
        teamListDao.deleteTeamItem(teamId)
    }

    override suspend fun isMatchFavorite(matchId: Int): Boolean {
        return matchListDao.getMatchItem(matchId) != null
    }

    override suspend fun addMatchItem(matchId: Int) {
        matchListDao.addMatchItem(
            MatchDbModel(matchId)
        )
    }

    override suspend fun deleteMatchItem(matchId: Int) {
        matchListDao.deleteMatchItem(matchId)
    }

    override suspend fun getAllFavoriteMatches(): List<MatchDto> {
        val matchIdsString = matchListDao.getMatchList().map { it.id }.joinToString(separator = ",")
        val result = if (matchIdsString != "")
            apiService.getMatchesByIds(matchIdsString).matches
        else
            arrayListOf()

        teamListDao.getTeamList().forEach {
            apiService.getTeamMatchesByDates(
                it.id,
                LocalDate.now().plusDays(-7).toString(),
                LocalDate.now().plusDays(7).toString()
            ).matches?.let { matchList -> result?.addAll(matchList) }
        }

        return result?.let {
            ArrayList(HashSet(it)).sortedBy { match ->
                match.utcDate?.let { dateString ->
                    Date(
                        dateString.slice(0..3).toInt() - 1900,
                        dateString.slice(5..6).toInt() - 1,
                        dateString.slice(8..9).toInt(),
                        dateString.slice(11..12).toInt(),
                        dateString.slice(14..15).toInt()
                    )
                }
            }
        } ?: throw RuntimeException()
    }
}