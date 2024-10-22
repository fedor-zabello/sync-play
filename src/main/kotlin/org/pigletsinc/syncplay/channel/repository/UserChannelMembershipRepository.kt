package org.pigletsinc.syncplay.channel.repository

import org.pigletsinc.syncplay.channel.entity.UserChannelMembership
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserChannelMembershipRepository : JpaRepository<UserChannelMembership, Long> {
}