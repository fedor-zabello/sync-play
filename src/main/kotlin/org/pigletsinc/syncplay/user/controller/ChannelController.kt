package org.pigletsinc.syncplay.user.controller

import org.pigletsinc.syncplay.user.ChannelCreateDto
import org.pigletsinc.syncplay.user.ChannelDto
import org.pigletsinc.syncplay.user.service.ChannelService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/channels")
class ChannelController(
    private val channelService: ChannelService,
) {
    @GetMapping()
    fun getChannelMembershipByEmail(principal: Principal): List<ChannelDto> = channelService.getChannelsForUser(principal)

    @PostMapping
    fun createChannel(
        @RequestBody dto: ChannelCreateDto,
        principal: Principal,
    ): ChannelDto = channelService.createChannelForUser(dto, principal)

    @DeleteMapping("/{id}")
    fun deleteChannel(@PathVariable id: Long) = channelService.deleteChannel(id)
}
