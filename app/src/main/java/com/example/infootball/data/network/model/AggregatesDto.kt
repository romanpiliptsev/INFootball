package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class AggregatesDto(
    @SerializedName("numberOfMatches") var numberOfMatches: Int? = null,
    @SerializedName("totalGoals") var totalGoals: Int? = null,
    @SerializedName("homeTeam") var homeTeam: TeamAggregatesDto? = TeamAggregatesDto(),
    @SerializedName("awayTeam") var awayTeam: TeamAggregatesDto? = TeamAggregatesDto()
)