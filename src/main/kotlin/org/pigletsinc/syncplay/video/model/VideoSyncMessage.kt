package org.pigletsinc.syncplay.video.model

data class VideoSyncMessage(
    val action: String,
    val time: Double,
    val clientId: String,
)
