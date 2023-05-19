package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class StandingsResponseDto(
    @SerializedName("area") var area: AreaDto? = AreaDto(),
    @SerializedName("competition") var competition: CompetitionDto? = CompetitionDto(),
    @SerializedName("standings") var standings: ArrayList<StandingsDto> = arrayListOf()
)