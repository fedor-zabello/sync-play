package org.pigletsinc.syncplay.domain.channel.exception

class ChannelNotFoundException : RuntimeException {
    constructor(message: String) : super(message)
}