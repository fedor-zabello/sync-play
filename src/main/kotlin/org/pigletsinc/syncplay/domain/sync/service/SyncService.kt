package org.pigletsinc.syncplay.domain.sync.service

import org.pigletsinc.syncplay.domain.sync.model.SyncState
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class SyncService {
    private val channelStates = ConcurrentHashMap<String, SyncState>()

    fun updateVideoSync(
        channelId: String,
        action: String,
        time: Long,
    ): SyncState {
        val state = channelStates.computeIfAbsent(channelId) { SyncState() }
        state.update(action, time)
        return state
    }

    fun updateSource(
        channelId: String,
        videoId: String,
    ): SyncState {
        val state = channelStates.computeIfAbsent(channelId) { SyncState() }
        state.videoId = videoId
        return state
    }

    fun getAllStates(): Map<String, SyncState> = channelStates
}
