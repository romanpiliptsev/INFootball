package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class ContractDto(
    @SerializedName("start") var start: String? = null,
    @SerializedName("until") var until: String? = null
)