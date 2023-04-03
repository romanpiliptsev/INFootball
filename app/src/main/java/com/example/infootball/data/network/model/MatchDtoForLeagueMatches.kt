package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class MatchDtoForLeagueMatches(
    @SerializedName("area") var area: AreaDto? = AreaDto(),
    @SerializedName("competition") var competition: CompetitionDto? = CompetitionDto(),
    @SerializedName("status") var status: String? = null
)