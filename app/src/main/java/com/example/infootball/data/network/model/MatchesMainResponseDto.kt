package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class MatchesMainResponseDto(
    @SerializedName("resultSet") val resultSet: MatchesResultSetDto? = null,
    @SerializedName("matches") val matches: ArrayList<MatchDto>? = null
)