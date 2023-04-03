package com.example.infootball.domain.entities

data class LeagueOfMatchesEntity(
    val leagueName: String,
    val leagueCode: String,
    val countryName: String,
    val leagueLogo: String,
    val countryFlag: String,
    val numberOfMatches: Int,
    val numberOfLiveMatches: Int
)