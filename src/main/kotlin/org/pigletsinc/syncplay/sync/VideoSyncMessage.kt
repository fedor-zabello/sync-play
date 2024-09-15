package org.pigletsinc.syncplay.sync

data class VideoSyncMessage(
    val action: String,
    val time: Double,
    val clientId: String,
)