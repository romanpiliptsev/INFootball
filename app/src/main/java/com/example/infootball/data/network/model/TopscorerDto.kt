package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class TopscorerDto(
    @SerializedName("player") var player: PlayerOfTeamDto? = null,
    @SerializedName("team") var team: TeamDto? = null,
    @SerializedName("playedMatches") var playedMatches: Int? = null,
    @SerializedName("goals") var goals: Int? = null,
    @SerializedName("assists") var assists: Int? = null,
    @SerializedName("penalties") var penalties: String? = null
)