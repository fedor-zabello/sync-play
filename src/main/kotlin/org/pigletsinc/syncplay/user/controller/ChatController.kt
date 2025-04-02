package org.pigletsinc.syncplay.user.controller

import org.pigletsinc.syncplay.user.chat.CommonMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller


@Controller
class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(@Payload chatMessage: CommonMessage): CommonMessage {
        println("Получено сообщение: ${chatMessage.messageId}: ${chatMessage.sender}: ${chatMessage.content}")
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