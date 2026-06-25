package org.pigletsinc.syncplay.application.sync.service

import org.pigletsinc.syncplay.application.sync.dto.SourceUrlDto
import org.pigletsinc.syncplay.application.sync.dto.VideoSyncDto
import org.pigletsinc.syncplay.domain.sync.service.SyncService
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SyncApplicationService(
    private val syncService: SyncService,
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    fun syncVideo(
        channelId: String,
        dto: VideoSyncDto,
    ): VideoSyncDto {
        syncService.updateVideoSync(channelId, dto.action, dto.time)
        return dto
    }

    fun syncSource(
        channelId: String,
        dto: SourceUrlDto,
    ): SourceUrlDto {
        syncService.updateSource(channelId, dto.videoId)
        return dto
    }

    @Scheduled(fixedRate = 10000)
    fun sendTimeSyncUpdates() {
        syncService.getAllStates().forEach { (channelId, state) ->
            val syncDto =
                VideoSyncDto(
                    action = state.action,
                    time = state.getCurrentTime(),
                    clientId = "SERVER",
                    videoId = state.videoId,
                )
            simpMessagingTemplate.convertAndSend("/topic/videoSync/$channelId", syncDto)
        }
    }
}
