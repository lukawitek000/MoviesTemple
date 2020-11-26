package com.lukasz.witkowski.android.moviestemple.models.responses

data class People(
        var cast: List<Actor>,
        var crew: List<CrewMember>
)