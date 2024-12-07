package org.pigletsinc.syncplay.user.controller

import org.pigletsinc.syncplay.user.ChannelDto
import org.pigletsinc.syncplay.user.service.UserChannelMembershipService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/channels")
class ChannelController(
    private val userChannelMembershipService: UserChannelMembershipService
) {

    @GetMapping()
    fun getChannelMembershipByEmail(principal: Principal): List<ChannelDto> {
        return userChannelMembershipService.getChannelsForUser(principal)
    }
}