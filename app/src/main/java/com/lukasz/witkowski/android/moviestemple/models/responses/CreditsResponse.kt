package com.lukasz.witkowski.android.moviestemple.models.responses

data class CreditsResponse(
        var cast: List<ActorResponse>,
        var crew: List<CrewMemberResponse>
)