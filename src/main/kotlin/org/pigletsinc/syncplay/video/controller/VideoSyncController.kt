package org.pigletsinc.syncplay.video.controller

import org.pigletsinc.syncplay.video.model.SourceUrlMessage
import org.pigletsinc.syncplay.video.model.VideoSyncMessage
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
