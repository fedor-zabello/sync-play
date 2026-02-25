package org.pigletsinc.syncplay.web.channel

import jakarta.validation.Valid
import org.pigletsinc.syncplay.application.channel.dto.ChannelCreateDto
import org.pigletsinc.syncplay.application.channel.dto.ChannelDto
import org.pigletsinc.syncplay.application.channel.dto.InviteMembersRequest
import org.pigletsinc.syncplay.application.channel.dto.RenameChannelRequest
import org.pigletsinc.syncplay.application.channel.service.ChannelApplicationService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/channels")
class ChannelController(
    private val channelApplicationService: ChannelApplicationService,
) {
    @GetMapping()
    fun getChannelMembershipByEmail(principal: Principal): List<ChannelDto> = channelApplicationService.getChannelsForUser(principal)

    @GetMapping("/{id}")
    fun getChannelDetails(
        @PathVariable id: Long,
    ): ChannelDto = channelApplicationService.getChannelById(id)

    @PostMapping
    fun createChannel(
        @Valid @RequestBody dto: ChannelCreateDto,
        principal: Principal,
    ): ChannelDto = channelApplicationService.createChannelForUser(dto, principal)

    @DeleteMapping("/{id}")
    fun deleteChannel(
        @PathVariable id: Long,
    ) = channelApplicationService.deleteChannel(id)

    @PutMapping("/{id}/rename")
    fun renameChannel(
        @PathVariable id: Long,
        @Valid @RequestBody request: RenameChannelRequest,
    ): ChannelDto = channelApplicationService.renameChannel(id, request.name)

    @PostMapping("/{id}/invite")
    fun inviteMembers(
        @PathVariable id: Long,
        @Valid @RequestBody request: InviteMembersRequest,
    ): ChannelDto = channelApplicationService.inviteMembers(id, request.usernames)
}
