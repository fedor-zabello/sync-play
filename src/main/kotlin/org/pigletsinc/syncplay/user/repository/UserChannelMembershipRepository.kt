package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.UserChannelMembership
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserChannelMembershipRepository : JpaRepository<UserChannelMembership, Long>
