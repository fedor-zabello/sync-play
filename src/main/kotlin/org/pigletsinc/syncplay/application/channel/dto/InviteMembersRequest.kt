package org.pigletsinc.syncplay.application.channel.dto

import jakarta.validation.constraints.NotEmpty

data class InviteMembersRequest(
    @field:NotEmpty
    val usernames: List<String>,
)
