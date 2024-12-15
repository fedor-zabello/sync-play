package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.Channel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChannelRepository : JpaRepository<Channel, Long>
