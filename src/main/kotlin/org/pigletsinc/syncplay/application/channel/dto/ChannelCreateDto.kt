package org.pigletsinc.syncplay.application.channel.dto

import jakarta.validation.constraints.NotBlank

data class ChannelCreateDto(
    @field:NotBlank
    val name: String,
)
