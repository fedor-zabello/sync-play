package org.pigletsinc.syncplay.synchronization.controller

import org.pigletsinc.syncplay.synchronization.model.SourceUrlMessage
import org.pigletsinc.syncplay.synchronization.model.VideoSyncMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/synchronization")
class VideoSyncController {
    @MessageMapping("/videoSync/{channelId}")
    @SendTo("/topic/videoSync/{channelId}")
    fun syncVideos(message: VideoSyncMessage): VideoSyncMessage = message

    @MessageMapping("/syncSource/{channelId}")
    @SendTo("/topic/syncSource/{channelId}")
    fun syncSource(message: SourceUrlMessage): SourceUrlMessage = message
}
