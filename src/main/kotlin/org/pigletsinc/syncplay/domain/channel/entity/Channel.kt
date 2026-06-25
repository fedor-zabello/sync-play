package org.pigletsinc.syncplay.domain.channel.entity

data class Channel(
    val id: Long?,
    val name: String,
    val subscribersCount: Int,
) {
    companion object {
        fun from(channel: Channel): Channel =
            Channel(
                id = channel.id,
                name = channel.name,
                subscribersCount = 0,
            )
    }
}