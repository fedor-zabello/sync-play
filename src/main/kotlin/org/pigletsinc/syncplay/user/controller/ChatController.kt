package org.pigletsinc.syncplay.user.controller

import org.pigletsinc.syncplay.user.chat.CommonMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable


@Controller
class ChatController {
    @MessageMapping("/chat.sendMessage/{channelId}")
    @SendTo("/topic/chat.sendMessage/{channelId}")
    fun sendMessage(@PathVariable channelId: String, @Payload chatMessage: CommonMessage): CommonMessage {
        println("Получено сообщение: ${channelId}: ${chatMessage.messageId}: ${chatMessage.sender}: ${chatMessage.content}")
        return chatMessage
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    fun addUser(
        @Payload chatMessage: CommonMessage,
        headerAccessor: SimpMessageHeaderAccessor
    ): CommonMessage {
        // Add username in web socket session
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender

        return chatMessage
    }
}