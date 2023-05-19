package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class MatchesResultSetDto(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("competitions") val competitions: String? = null,
    @SerializedName("first") val first: String? = null,
    @SerializedName("last") val last: String? = null,
    @SerializedName("played") val played: Int? = null
)