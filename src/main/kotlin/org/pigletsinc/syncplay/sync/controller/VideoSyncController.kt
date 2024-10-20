package org.pigletsinc.syncplay.sync.controller

import org.pigletsinc.syncplay.sync.model.SourceUrlMessage
import org.pigletsinc.syncplay.sync.model.VideoSyncMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/videos")
class VideoSyncController {
    @MessageMapping("/videoSync")
    @SendTo("/topic/videoSync")
    fun syncVideos(message: VideoSyncMessage): VideoSyncMessage = message

    @MessageMapping("/syncSource")
    @SendTo("/topic/syncSource")
    fun syncSource(message: SourceUrlMessage): SourceUrlMessage = message
}
