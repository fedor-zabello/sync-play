package org.pigletsinc.syncplay.application.channel.dto

import jakarta.validation.constraints.NotBlank

data class RenameChannelRequest(
    @field:NotBlank
    val name: String,
)
