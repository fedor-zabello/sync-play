package org.pigletsinc.syncplay.application.channel.service

import org.pigletsinc.syncplay.application.channel.dto.ChannelCreateDto
import org.pigletsinc.syncplay.application.channel.dto.ChannelDto
import org.pigletsinc.syncplay.application.channel.mapper.toDto
import org.pigletsinc.syncplay.domain.channel.service.ChannelService
import org.pigletsinc.syncplay.domain.user.service.UserService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class ChannelApplicationService(
    private val userService: UserService,
    private val channelService: ChannelService,
) {
    fun getChannelsForUser(principal: Principal): List<ChannelDto> {
        val userProfile = userService.getUserProfileByPrincipal(principal)
        return userProfile.channels.map { it.toDto() }
    }

    fun getChannelById(id: Long): ChannelDto = channelService.getChannelById(id).toDto()

    fun createChannelForUser(
        channelCreateDto: ChannelCreateDto,
        principal: Principal,
    ): ChannelDto {
        val userProfile = userService.getUserProfileByPrincipal(principal)
        val channel = channelService.createChannel(channelCreateDto.name)
        userService.saveUserProfile(userProfile.copy(channels = userProfile.channels + channel))
        return channel.toDto()
    }

    fun deleteChannel(id: Long) = channelService.deleteChannel(id)

    fun renameChannel(
        channelId: Long,
        newName: String,
    ): ChannelDto = channelService.renameChannel(channelId, newName).toDto()

    fun inviteMembers(
        channelId: Long,
        usernames: List<String>,
    ): ChannelDto = channelService.addMembersToChannel(channelId, usernames).toDto()
}
