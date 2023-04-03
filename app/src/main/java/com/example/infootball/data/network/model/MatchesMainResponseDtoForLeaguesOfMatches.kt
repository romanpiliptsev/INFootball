package com.example.infootball.data.network.model

data class MatchesMainResponseDtoForLeaguesOfMatches(
    val resultSet: MatchesResultSetDto? = null,
    val matches: ArrayList<MatchDtoForLeagueMatches>? = null
)