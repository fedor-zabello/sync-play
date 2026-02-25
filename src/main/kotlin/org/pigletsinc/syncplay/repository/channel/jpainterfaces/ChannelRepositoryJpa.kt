package org.pigletsinc.syncplay.repository.channel.jpainterfaces

import org.pigletsinc.syncplay.repository.channel.entity.ChannelJpa
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ChannelRepositoryJpa : JpaRepository<ChannelJpa, Long> {
    override fun findById(id: Long): Optional<ChannelJpa?>
}
