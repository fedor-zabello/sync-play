package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, Long> {
}
