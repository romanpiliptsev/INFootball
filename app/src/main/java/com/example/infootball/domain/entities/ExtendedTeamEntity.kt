package com.example.infootball.domain.entities

import com.example.infootball.data.network.model.AreaDto
import com.example.infootball.data.network.model.CoachDto
import com.example.infootball.data.network.model.PlayerOfTeamDto

data class ExtendedTeamEntity(
    var area: AreaDto?,
    var id: Int?,
    var name: String?,
    var shortName: String?,
    var tla: String?,
    var crest: String?,
    var address: String?,
    var website: String?,
    var founded: Int?,
    var clubColors: String?,
    var venue: String?,
    var leagueCode: String,
    var coach: CoachDto?,
    var squad: ArrayList<PlayerOfTeamDto>
)