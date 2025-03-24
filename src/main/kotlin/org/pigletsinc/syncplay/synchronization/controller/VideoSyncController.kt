package org.pigletsinc.syncplay.synchronization.controller

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
    // Inject the messaging template
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    // Store state for each channel
    private val channelStates = ConcurrentHashMap<String, ChannelState>()

    @MessageMapping("/videoSync/{channelId}")
    @SendTo("/topic/videoSync/{channelId}")
    fun syncVideos(@DestinationVariable channelId: String, message: VideoSyncMessage): VideoSyncMessage {
        // Get or create channel state
        val state = channelStates.computeIfAbsent(channelId) { ChannelState() }

        // Update state with new information
        state.update(message.action, message.time)

        // Return the message to broadcast to all clients
        return message
    }

    @MessageMapping("/syncSource/{channelId}")
    @SendTo("/topic/syncSource/{channelId}")
    fun syncSource(@DestinationVariable channelId: String, message: SourceUrlMessage): SourceUrlMessage {
        // Get or create channel state
        val state = channelStates.computeIfAbsent(channelId) { ChannelState() }

        // Update video ID in the state
        state.videoId = message.videoId

        return message
    }

    // Optional: Periodic time sync for long-running videos
//    @Scheduled(fixedRate = 10000) // Every 10 seconds
//    fun sendTimeSyncUpdates() {
//        println("Sending time sync updates. Channels=${channelStates.keys}")
//        channelStates.forEach { (channelId, state) ->
//            val syncMessage = VideoSyncMessage(
//                action = state.action,
//                time = state.getCurrentTime(),
//                clientId = "SERVER"
//            )
//            // Send time sync message to clients
//            simpMessagingTemplate.convertAndSend("/topic/videoSync/$channelId", syncMessage)
//        }
//    }
}

// State class to track channel information
class ChannelState {
    var action: String = "pause"
    var time: Double = 0.0
    var videoId: String? = null
    var lastUpdateTime: Long = System.currentTimeMillis()

    fun update(action: String?, time: Double?) {
        if (action != null) this.action = action
        if (time != null) this.time = time
        this.lastUpdateTime = System.currentTimeMillis()
    }

    fun getCurrentTime(): Double {
        return if (action == "play") {
            // Calculate expected current time based on elapsed time
            val elapsedSeconds = (System.currentTimeMillis() - lastUpdateTime) / 1000.0
            time + elapsedSeconds
        } else {
            time
        }
    }
}