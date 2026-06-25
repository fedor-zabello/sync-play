package org.pigletsinc.syncplay.application.sync.dto

data class VideoSyncDto(
    val action: String,
    val time: Long,
    val clientId: String,
    val videoId: String?,
)
