package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class ScoreDto(
    @SerializedName("winner") var winner: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("fullTime") var fullTime: FullTimeDto? = FullTimeDto(),
    @SerializedName("halfTime") var halfTime: HalfTimeDto? = HalfTimeDto()
)
