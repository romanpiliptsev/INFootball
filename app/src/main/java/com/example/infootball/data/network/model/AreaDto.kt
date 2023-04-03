package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class AreaDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("flag") var flag: String? = null
)