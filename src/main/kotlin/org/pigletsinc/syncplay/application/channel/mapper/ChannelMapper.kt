package org.pigletsinc.syncplay.application.channel.mapper

import org.pigletsinc.syncplay.application.channel.dto.ChannelDto
import org.pigletsinc.syncplay.domain.channel.entity.Channel

fun Channel.toDto(): ChannelDto = ChannelDto(id = this.id, name = this.name, subscribersCount = this.subscribersCount)

