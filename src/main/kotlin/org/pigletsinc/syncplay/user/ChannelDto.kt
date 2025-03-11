package org.pigletsinc.syncplay.user

import org.pigletsinc.syncplay.user.entity.Channel

data class ChannelDto(
    val id: Long?,
    val name: String,
    val subscribersCount: Int,
) {
    companion object {
        fun from(channel: Channel): ChannelDto {
            return ChannelDto(
                id = channel.id,
                name = channel.name,
                subscribersCount = 0
            )
        }
    }
}
