package com.example.infootball.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerOfTeamDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("position") var position: String? = null,
    @SerializedName("dateOfBirth") var dateOfBirth: String? = null,
    @SerializedName("nationality") var nationality: String? = null
) : Parcelable