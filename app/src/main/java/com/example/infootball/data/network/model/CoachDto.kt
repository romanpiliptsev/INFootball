package com.example.infootball.data.network.model

import com.google.gson.annotations.SerializedName

data class CoachDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("dateOfBirth") var dateOfBirth: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("contract") var contract: ContractDto? = ContractDto()
)