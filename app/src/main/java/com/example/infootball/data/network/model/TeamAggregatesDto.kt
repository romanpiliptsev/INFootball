package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class TeamAggregatesDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("wins") var wins: Int? = null,
    @SerializedName("draws") var draws: Int? = null,
    @SerializedName("losses") var losses: Int? = null
)