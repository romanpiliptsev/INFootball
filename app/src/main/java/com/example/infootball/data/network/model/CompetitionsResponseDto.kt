package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class CompetitionsResponseDto(
    @SerializedName("competitions") var competitions: ArrayList<CompetitionWithAreaDto>? = null
)