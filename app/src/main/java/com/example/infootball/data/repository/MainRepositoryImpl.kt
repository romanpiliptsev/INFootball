package com.example.infootball.data.repository

import com.example.infootball.data.network.ApiFactory
import com.example.infootball.data.network.model.LeagueResponseDto
import com.example.infootball.domain.MainRepository

class MainRepositoryImpl : MainRepository {

    private val apiService = ApiFactory.apiService

    override suspend fun getLeagueResponseById(id: Int): LeagueResponseDto {
        val mainResponse = apiService.getLeagueById(id)
        return mainResponse.response?.get(0) ?: throw Exception()
    }

    override suspend fun getLeagueList(idList: List<Int>): List<LeagueResponseDto> {
        val returnList = arrayListOf<LeagueResponseDto>()

        idList.forEach {
            returnList.add(getLeagueResponseById(it))
        }
        return returnList
    }
}