package org.pigletsinc.syncplay.user.service

import org.pigletsinc.syncplay.user.ChannelDto
import org.pigletsinc.syncplay.user.toDto
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserChannelMembershipService(
    private val userService: UserService,
) {
    fun getChannelsForUser(principal: Principal): List<ChannelDto> {
        val userProfile = userService.getUserProfileByPrincipal(principal)
        return userProfile.channels.map { it.toDto() }
    }
}
