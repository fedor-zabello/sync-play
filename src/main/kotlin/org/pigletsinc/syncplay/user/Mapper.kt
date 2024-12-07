package org.pigletsinc.syncplay.user

import org.pigletsinc.syncplay.user.entity.Channel
import org.pigletsinc.syncplay.user.entity.UserProfile

fun Channel.toDto(): ChannelDto = ChannelDto(id = this.id, name = this.name)

fun UserProfile.toDto(): UserDto = UserDto(this.name)