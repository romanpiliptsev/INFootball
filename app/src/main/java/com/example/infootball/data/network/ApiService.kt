package com.example.infootball.data.network

import com.example.infootball.data.network.model.LeagueMainResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(LEAGUES)
    suspend fun getLeagueById(@Query("id") id: Int): LeagueMainResponseDto

    companion object {
        private const val LEAGUES = "leagues"
    }
}