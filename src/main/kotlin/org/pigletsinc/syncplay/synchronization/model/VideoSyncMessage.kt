package org.pigletsinc.syncplay.synchronization.model

data class VideoSyncMessage(
    val action: String,
    val time: Double,
    val clientId: String,
)
