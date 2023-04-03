package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class SeasonDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("startDate") var startDate: String? = null,
    @SerializedName("endDate") var endDate: String? = null,
    @SerializedName("currentMatchday") var currentMatchday: Int? = null,
    @SerializedName("winner") var winner: String? = null
)