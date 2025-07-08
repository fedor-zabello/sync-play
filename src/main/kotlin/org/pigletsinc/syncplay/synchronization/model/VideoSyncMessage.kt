package org.pigletsinc.syncplay.synchronization.model

data class VideoSyncMessage(
    val action: String,
    val time: Long,
    val clientId: String,
    val videoId: String?,
)
