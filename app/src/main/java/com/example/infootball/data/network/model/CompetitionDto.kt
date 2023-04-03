package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class CompetitionDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("emblem") var emblem: String? = null
)