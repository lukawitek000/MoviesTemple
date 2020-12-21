package com.lukasz.witkowski.android.moviestemple.api.responses

data class CreditsResponse(
        var cast: List<ActorResponse>,
        var crew: List<CrewMemberResponse>
)