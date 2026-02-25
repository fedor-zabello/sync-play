package org.pigletsinc.syncplay.application.channel.dto

data class ChannelDto(
    val id: Long?,
    val name: String,
    val subscribersCount: Int,
)
