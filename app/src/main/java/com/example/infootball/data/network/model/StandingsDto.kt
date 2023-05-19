package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class StandingsDto(
    @SerializedName("stage") var stage: String? = null,
    @SerializedName("group") var group: String? = null,
    @SerializedName("table") var table: ArrayList<TablePositionDto> = arrayListOf()
)