package com.example.infootball.data.network

import com.example.infootball.data.network.model.MatchesMainResponseDto
import com.example.infootball.data.network.model.MatchesMainResponseDtoForLeaguesOfMatches
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(MATCHES)
    suspend fun getMatchesOfLeagueDays(
        @Query("competitions") competitions: String,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchesMainResponseDto

    @GET(MATCHES)
    suspend fun getMatchesForLeaguesOfMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchesMainResponseDtoForLeaguesOfMatches

    companion object {
        private const val MATCHES = "matches"
    }
}