package com.example.infootball.data.network.model

data class MatchesMainResponseDto(
    val resultSet: MatchesResultSetDto? = null,
    val matches: ArrayList<MatchDto>? = null
)