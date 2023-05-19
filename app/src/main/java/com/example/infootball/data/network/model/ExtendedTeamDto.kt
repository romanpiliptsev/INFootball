package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class ExtendedTeamDto(
    @SerializedName("area") var area: AreaDto? = AreaDto(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("shortName") var shortName: String? = null,
    @SerializedName("tla") var tla: String? = null,
    @SerializedName("crest") var crest: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("website") var website: String? = null,
    @SerializedName("founded") var founded: Int? = null,
    @SerializedName("clubColors") var clubColors: String? = null,
    @SerializedName("venue") var venue: String? = null,
    @SerializedName("runningCompetitions") var runningCompetitions: ArrayList<CompetitionDto> = arrayListOf(),
    @SerializedName("coach") var coach: CoachDto? = CoachDto(),
    @SerializedName("squad") var squad: ArrayList<PlayerOfTeamDto> = arrayListOf()
)