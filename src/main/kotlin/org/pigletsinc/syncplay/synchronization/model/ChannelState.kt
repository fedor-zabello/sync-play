package org.pigletsinc.syncplay.synchronization.model

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