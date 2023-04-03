package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("shortName") var shortName: String? = null,
    @SerializedName("tla") var tla: String? = null,
    @SerializedName("crest") var crest: String? = null
)