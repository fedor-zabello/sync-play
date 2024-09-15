package org.pigletsinc.syncplay.sync.controller

import org.pigletsinc.syncplay.sync.model.VideoSyncMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class VideoSyncController {

    @MessageMapping("/syncVideo")
    @SendTo("/topic/videoSync")
    fun syncVideos(message: VideoSyncMessage): VideoSyncMessage {
        return message
    }
}