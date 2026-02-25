package org.pigletsinc.syncplay.web.chat

import org.pigletsinc.syncplay.application.chat.dto.MessageDto
import org.pigletsinc.syncplay.application.chat.service.ChatApplicationService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class ChatController(
    private val chatApplicationService: ChatApplicationService,
) {
    @MessageMapping("/chat.sendMessage/{channelId}")
    @SendTo("/topic/chat.sendMessage/{channelId}")
    fun sendMessage(
        @DestinationVariable channelId: String,
        @Payload message: MessageDto,
    ): MessageDto = chatApplicationService.handleMessage(message)

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    fun addUser(
        @Payload message: MessageDto,
        headerAccessor: SimpMessageHeaderAccessor,
    ): MessageDto {
        headerAccessor.sessionAttributes!!["username"] = message.sender
        return chatApplicationService.handleMessage(message)
    }
}
