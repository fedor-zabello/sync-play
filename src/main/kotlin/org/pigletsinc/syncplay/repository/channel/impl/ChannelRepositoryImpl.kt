package org.pigletsinc.syncplay.repository.channel.impl

import jakarta.persistence.EntityNotFoundException
import org.pigletsinc.syncplay.domain.channel.entity.Channel
import org.pigletsinc.syncplay.domain.channel.repository.IChannelRepository
import org.pigletsinc.syncplay.repository.channel.jpainterfaces.ChannelRepositoryJpa
import org.pigletsinc.syncplay.repository.channel.mapper.toChannel
import org.pigletsinc.syncplay.repository.channel.mapper.toJpa
import org.springframework.stereotype.Repository

@Repository
class ChannelRepositoryImpl(
    private val channelRepositoryJpa: ChannelRepositoryJpa,
) : IChannelRepository {
    override fun findById(id: Long): Channel {
        val channelJpa =
            channelRepositoryJpa.findById(id)
                .orElseThrow { EntityNotFoundException("Channel with id $id not found") }
        return channelJpa!!.toChannel()
    }

    override fun save(channel: Channel): Channel {
        val jpa = channel.toJpa()
        val saved = channelRepositoryJpa.save(jpa)
        return saved.toChannel()
    }

    override fun deleteById(id: Long) {
        channelRepositoryJpa.deleteById(id)
    }

    override fun findAll(): List<Channel> = channelRepositoryJpa.findAll().map { it.toChannel() }
}
