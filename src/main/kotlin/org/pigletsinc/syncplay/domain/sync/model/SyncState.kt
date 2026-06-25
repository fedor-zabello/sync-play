package org.pigletsinc.syncplay.domain.sync.model

class SyncState {
    var action: String = "pause"
    var time: Long = 0
    var videoId: String? = null
    var lastUpdateTime: Long = System.currentTimeMillis()

    fun update(
        action: String?,
        time: Long?,
    ) {
        if (action != null) this.action = action
        if (time != null) this.time = time
        this.lastUpdateTime = System.currentTimeMillis()
    }

    fun getCurrentTime(): Long =
        if (action == "play") {
            val elapsedSeconds = (System.currentTimeMillis() - lastUpdateTime) / 1000
            time + elapsedSeconds
        } else {
            time
        }
}
