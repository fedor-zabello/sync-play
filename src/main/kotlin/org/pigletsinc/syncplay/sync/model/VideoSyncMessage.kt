package org.pigletsinc.syncplay.sync.model

data class VideoSyncMessage(
    val action: String,
    val time: Double,
    val clientId: String,
)
