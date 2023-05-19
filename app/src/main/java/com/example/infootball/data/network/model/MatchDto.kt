package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("area") var area: AreaDto? = AreaDto(),
    @SerializedName("competition") var competition: CompetitionDto? = CompetitionDto(),
    @SerializedName("season") var season: SeasonDto? = SeasonDto(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("utcDate") var utcDate: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("matchday") var matchday: Int? = null,
    @SerializedName("stage") var stage: String? = null,
    @SerializedName("group") var group: String? = null,
    @SerializedName("homeTeam") var homeTeam: TeamDto? = TeamDto(),
    @SerializedName("awayTeam") var awayTeam: TeamDto? = TeamDto(),
    @SerializedName("score") var score: ScoreDto? = ScoreDto(),
    @SerializedName("referees") var referees: ArrayList<RefereeDto> = arrayListOf(),
    @SerializedName("venue") var venue: String? = null
)