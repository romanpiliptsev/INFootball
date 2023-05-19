package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class RefereeDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("nationality") var nationality: String? = null
)