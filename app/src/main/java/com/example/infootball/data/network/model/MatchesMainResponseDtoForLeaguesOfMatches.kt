package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class MatchesMainResponseDtoForLeaguesOfMatches(
    @SerializedName("resultSet") val resultSet: MatchesResultSetDto? = null,
    @SerializedName("matches") val matches: ArrayList<MatchDtoForLeagueMatches>? = null
)