package com.example.infootball.data.network

import com.example.infootball.data.network.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(MATCHES)
    suspend fun getMatchesOfLeagueDays(
        @Query(COMPETITIONS) competitions: String,
        @Query(DATE_FROM) dateFrom: String,
        @Query(DATE_TO) dateTo: String
    ): MatchesMainResponseDto

    @GET(MATCHES)
    suspend fun getMatchesForLeaguesOfMatches(
        @Query(DATE_FROM) dateFrom: String,
        @Query(DATE_TO) dateTo: String
    ): MatchesMainResponseDtoForLeaguesOfMatches

    @GET("$MATCHES/{$ID}")
    suspend fun getMatch(
        @Path(ID) id: Int
    ): MatchDto

    @GET("$MATCHES/{$ID}/$H2H")
    suspend fun getMatchH2H(
        @Path(ID) id: Int
    ): H2HDto

    @GET("$COMPETITIONS/{$COMPETITION_CODE}/$STANDINGS")
    suspend fun getCompetitionStandings(
        @Path(COMPETITION_CODE) code: String,
        @Query(SEASON) season: String
    ): StandingsResponseDto

    @GET("$TEAMS/{$TEAM_ID}")
    suspend fun getTeamById(
        @Path(TEAM_ID) teamId: Int
    ): ExtendedTeamDto

    @GET("$TEAMS/{$TEAM_ID}/$MATCHES/?$STATUS=$STATUS_FINISHED")
    suspend fun getFinishedMatchesOfTeam(
        @Path(TEAM_ID) teamId: Int
    ): MatchesMainResponseDto

    @GET("$TEAMS/{$TEAM_ID}/$MATCHES/?$STATUS=$STATUS_TIMED,$STATUS_SCHEDULED,$STATUS_LIVE,$STATUS_IN_PLAY,$STATUS_PAUSED,$STATUS_POSTPONED,$STATUS_SUSPENDED,$STATUS_CANCELLED")
    suspend fun getNotFinishedMatchesOfTeam(
        @Path(TEAM_ID) teamId: Int
    ): MatchesMainResponseDto

    @GET("$MATCHES?$STATUS=$STATUS_LIVE,$STATUS_IN_PLAY,$STATUS_PAUSED,$STATUS_SUSPENDED")
    suspend fun getLiveMatches(): MatchesMainResponseDto

    @GET(COMPETITIONS)
    suspend fun getCompetitions(): CompetitionsResponseDto

    @GET("$COMPETITIONS/{$LEAGUE_CODE}")
    suspend fun getCompetition(
        @Path(LEAGUE_CODE) leagueCode: String
    ): CompetitionWithAreaDto

    @GET("$COMPETITIONS/{$LEAGUE_CODE}/$MATCHES")
    suspend fun getCompetitionMatches(
        @Path(LEAGUE_CODE) leagueCode: String,
        @Query(SEASON) season: String,
        @Query(STATUS) status: String
    ): MatchesMainResponseDto

    @GET("$COMPETITIONS/{$LEAGUE_CODE}/$SCORERS")
    suspend fun getCompetitionTopscorers(
        @Path(LEAGUE_CODE) leagueCode: String,
        @Query(LIMIT) limit: Int,
        @Query(SEASON) season: String
    ): TopscorersResponseDto

    @GET(MATCHES)
    suspend fun getMatchesByIds(
        @Query(IDS) ids: String
    ): MatchesMainResponseDto

    @GET("$TEAMS/{$TEAM_ID}/$MATCHES/")
    suspend fun getTeamMatchesByDates(
        @Path(TEAM_ID) teamId: Int,
        @Query(DATE_FROM) dateFrom: String,
        @Query(DATE_TO) dateTo: String
    ): MatchesMainResponseDto

    companion object {
        private const val MATCHES = "matches"
        private const val ID = "id"
        private const val DATE_FROM = "dateFrom"
        private const val DATE_TO = "dateTo"
        private const val SEASON = "season"
        private const val H2H = "head2head"
        private const val COMPETITIONS = "competitions"
        private const val COMPETITION_CODE = "competition_code"
        private const val STANDINGS = "standings"
        private const val TEAMS = "teams"
        private const val TEAM_ID = "team_id"
        private const val LEAGUE_CODE = "league_code"
        private const val STATUS = "status"
        private const val SCORERS = "scorers"
        private const val LIMIT = "limit"
        private const val IDS = "ids"

        const val STATUS_FINISHED = "FINISHED"
        const val STATUS_TIMED = "TIMED"
        const val STATUS_SCHEDULED = "SCHEDULED"
        const val STATUS_LIVE = "LIVE"
        const val STATUS_IN_PLAY = "IN_PLAY"
        const val STATUS_PAUSED = "PAUSED"
        const val STATUS_POSTPONED = "POSTPONED"
        const val STATUS_SUSPENDED = "SUSPENDED"
        const val STATUS_CANCELLED = "CANCELLED"
    }
}