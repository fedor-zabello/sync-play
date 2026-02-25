package org.pigletsinc.syncplay.domain.channel.repository

import org.pigletsinc.syncplay.domain.channel.entity.Channel

interface IChannelRepository {
    fun findById(id: Long): Channel
    fun save(channel: Channel): Channel
    fun deleteById(id: Long)
    fun findAll(): List<Channel>
}