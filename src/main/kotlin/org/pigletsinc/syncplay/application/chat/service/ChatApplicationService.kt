package org.pigletsinc.syncplay.application.chat.service

import org.pigletsinc.syncplay.application.chat.dto.MessageDto
import org.springframework.stereotype.Service

@Service
class ChatApplicationService {
    fun handleMessage(message: MessageDto): MessageDto = message
}
