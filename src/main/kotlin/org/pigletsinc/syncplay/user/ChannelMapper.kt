package org.pigletsinc.syncplay.user

import org.pigletsinc.syncplay.user.entity.Channel

fun Channel.toDto(): ChannelDto = ChannelDto(id = this.id, name = this.name)