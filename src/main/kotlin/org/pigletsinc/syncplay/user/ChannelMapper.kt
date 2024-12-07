package org.pigletsinc.syncplay.user

import org.mapstruct.Mapper
import org.pigletsinc.syncplay.user.entity.Channel

@Mapper(componentModel = "spring")
interface ChannelMapper {
    fun toDto(entity: Channel): ChannelDto
}
