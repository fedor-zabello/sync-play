package org.pigletsinc.syncplay.domain.user.entity

import org.pigletsinc.syncplay.domain.channel.entity.Channel
import java.time.LocalDateTime

data class UserProfile(
    val id: Long? = null,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val channels: Set<Channel> = emptySet(),
)
