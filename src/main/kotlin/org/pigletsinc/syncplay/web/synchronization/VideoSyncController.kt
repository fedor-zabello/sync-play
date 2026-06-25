package org.pigletsinc.syncplay.web.synchronization

import org.pigletsinc.syncplay.application.sync.dto.SourceUrlDto
import org.pigletsinc.syncplay.application.sync.dto.VideoSyncDto
import org.pigletsinc.syncplay.application.sync.service.SyncApplicationService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/synchronization")
class VideoSyncController(
    private val syncApplicationService: SyncApplicationService,
) {
    @MessageMapping("/videoSync/{channelId}")
    @SendTo("/topic/videoSync/{channelId}")
    fun syncVideos(
        @DestinationVariable channelId: String,
        message: VideoSyncDto,
    ): VideoSyncDto = syncApplicationService.syncVideo(channelId, message)

    @MessageMapping("/syncSource/{channelId}")
    @SendTo("/topic/syncSource/{channelId}")
    fun syncSource(
        @DestinationVariable channelId: String,
        message: SourceUrlDto,
    ): SourceUrlDto = syncApplicationService.syncSource(channelId, message)
}
