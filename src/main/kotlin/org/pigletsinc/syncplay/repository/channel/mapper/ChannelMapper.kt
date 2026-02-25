package org.pigletsinc.syncplay.repository.channel.mapper

import org.pigletsinc.syncplay.domain.channel.entity.Channel
import org.pigletsinc.syncplay.repository.channel.entity.ChannelJpa

fun ChannelJpa.toChannel(): Channel = Channel(id = this.id, name = this.name, subscribersCount = this.userProfiles.size)

fun Channel.toJpa(): ChannelJpa = ChannelJpa(id = this.id, name = this.name)
