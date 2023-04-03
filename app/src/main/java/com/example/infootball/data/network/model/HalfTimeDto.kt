package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class HalfTimeDto(
    @SerializedName("home") var home: Int? = null,
    @SerializedName("away") var away: Int? = null
)
