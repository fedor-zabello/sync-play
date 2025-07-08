package org.pigletsinc.syncplay.synchronization.controller

import org.pigletsinc.syncplay.synchronization.model.ChannelState
import org.pigletsinc.syncplay.synchronization.model.SourceUrlMessage
import org.pigletsinc.syncplay.synchronization.model.VideoSyncMessage
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.concurrent.ConcurrentHashMap

@Controller
@RequestMapping("/api/v1/synchronization")
class VideoSyncController(
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    private val channelStates = ConcurrentHashMap<String, ChannelState>()

    @MessageMapping("/videoSync/{channelId}")
    @SendTo("/topic/videoSync/{channelId}")
    fun syncVideos(
        @DestinationVariable channelId: String,
        message: VideoSyncMessage,
    ): VideoSyncMessage {
        // Get or create channel state
        val state = channelStates.computeIfAbsent(channelId) { ChannelState() }

        // Update state with new information
        state.update(message.action, message.time)

        // Return the message to broadcast to all clients
        return message
    }

    @MessageMapping("/syncSource/{channelId}")
    @SendTo("/topic/syncSource/{channelId}")
    fun syncSource(
        @DestinationVariable channelId: String,
        message: SourceUrlMessage,
    ): SourceUrlMessage {
        // Get or create channel state
        val state = channelStates.computeIfAbsent(channelId) { ChannelState() }

        // Update video ID in the state
        state.videoId = message.videoId

        return message
    }

    @Scheduled(fixedRate = 10000)
    fun sendTimeSyncUpdates() {
        channelStates.forEach { (channelId, state) ->
            val syncMessage =
                VideoSyncMessage(
                    action = state.action,
                    time = state.getCurrentTime(),
                    clientId = "SERVER",
                    videoId = state.videoId,
                )
            // Send time sync message to clients
            simpMessagingTemplate.convertAndSend("/topic/videoSync/$channelId", syncMessage)
        }
    }
}
