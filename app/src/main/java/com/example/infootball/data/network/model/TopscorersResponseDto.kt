package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class TopscorersResponseDto(
//    @SerializedName("competition") var competition: CompetitionDto? = null,
    @SerializedName("scorers") var scorers: ArrayList<TopscorerDto> = arrayListOf()
)