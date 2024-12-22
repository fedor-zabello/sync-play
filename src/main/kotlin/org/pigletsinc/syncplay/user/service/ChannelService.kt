package org.pigletsinc.syncplay.user.service

import jakarta.persistence.EntityNotFoundException
import org.pigletsinc.syncplay.user.ChannelCreateDto
import org.pigletsinc.syncplay.user.ChannelDto
import org.pigletsinc.syncplay.user.entity.Channel
import org.pigletsinc.syncplay.user.repository.ChannelRepository
import org.pigletsinc.syncplay.user.toDto
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class ChannelService(
    private val userService: UserService,
    private val channelRepository: ChannelRepository,
) {
    fun getChannelsForUser(principal: Principal): List<ChannelDto> {
        val userProfile = userService.getUserProfileByPrincipal(principal)
        return userProfile.channels.map { it.toDto() }
    }

    fun getChannelById(id: Long): ChannelDto {
        var channel = channelRepository.findById(id).orElseThrow { EntityNotFoundException("Channel with id $id not found") }
        return channel.toDto()
    }

    fun createChannelForUser(
        channelCreateDto: ChannelCreateDto,
        principal: Principal,
    ): ChannelDto {
        val userProfile = userService.getUserProfileByPrincipal(principal)
        val channel = channelRepository.save(Channel(name = channelCreateDto.name))
        userProfile.channels.add(channel)
        userService.saveUserProfile(userProfile)
        return channel.toDto()
    }

    fun deleteChannel(id: Long) {
        channelRepository.deleteById(id)
    }
}
