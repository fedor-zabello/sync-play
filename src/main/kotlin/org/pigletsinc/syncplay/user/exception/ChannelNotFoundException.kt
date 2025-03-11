package org.pigletsinc.syncplay.user.exception

class ChannelNotFoundException : RuntimeException {
    constructor(message: String) : super(message)
}