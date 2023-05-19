package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class MatchDtoForH2H(
    @SerializedName("competition") var competition: CompetitionDto? = CompetitionDto(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("utcDate") var utcDate: String? = null,
    @SerializedName("homeTeam") var homeTeam: TeamDto? = TeamDto(),
    @SerializedName("awayTeam") var awayTeam: TeamDto? = TeamDto(),
    @SerializedName("score") var score: ScoreDto? = ScoreDto()
)