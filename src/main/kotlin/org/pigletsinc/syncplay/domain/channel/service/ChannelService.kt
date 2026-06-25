package org.pigletsinc.syncplay.domain.channel.service

import org.pigletsinc.syncplay.domain.channel.entity.Channel
import org.pigletsinc.syncplay.domain.channel.repository.IChannelRepository
import org.pigletsinc.syncplay.domain.user.service.UserService
import org.springframework.stereotype.Service

@Service
class ChannelService(
    private val channelRepository: IChannelRepository,
    private val userService: UserService,
) {
    fun getChannelById(id: Long): Channel = channelRepository.findById(id)

    fun createChannel(name: String): Channel = channelRepository.save(Channel(id = null, name = name, subscribersCount = 0))

    fun deleteChannel(id: Long) = channelRepository.deleteById(id)

    fun renameChannel(
        channelId: Long,
        newName: String,
    ): Channel {
        val channel = channelRepository.findById(channelId)
        return channelRepository.save(channel.copy(name = newName))
    }

    fun addMembersToChannel(
        channelId: Long,
        usernames: List<String>,
    ): Channel {
        val channel = channelRepository.findById(channelId)
        usernames
            .mapNotNull { username -> userService.findUserProfileByName(username) }
            .forEach { userProfile -> userService.saveUserProfile(userProfile.copy(channels = userProfile.channels + channel)) }
        return channelRepository.findById(channelId)
    }
}
