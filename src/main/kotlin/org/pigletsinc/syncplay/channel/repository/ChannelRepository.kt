package org.pigletsinc.syncplay.channel.repository

import org.pigletsinc.syncplay.channel.entity.Channel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChannelRepository : JpaRepository<Channel, Long>
