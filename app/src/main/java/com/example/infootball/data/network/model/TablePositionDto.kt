package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class TablePositionDto(
    @SerializedName("position") var position: Int? = null,
    @SerializedName("team") var team: TeamDto? = TeamDto(),
    @SerializedName("playedGames") var playedGames: Int? = null,
    @SerializedName("form") var form: String? = null,
    @SerializedName("won") var won: Int? = null,
    @SerializedName("draw") var draw: Int? = null,
    @SerializedName("lost") var lost: Int? = null,
    @SerializedName("points") var points: Int? = null,
    @SerializedName("goalsFor") var goalsFor: Int? = null,
    @SerializedName("goalsAgainst") var goalsAgainst: Int? = null,
    @SerializedName("goalDifference") var goalDifference: Int? = null
)