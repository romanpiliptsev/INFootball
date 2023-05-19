package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class H2HDto(
    @SerializedName("aggregates") var aggregates: AggregatesDto? = AggregatesDto(),
    @SerializedName("matches") var matches: ArrayList<MatchDtoForH2H> = arrayListOf()
)