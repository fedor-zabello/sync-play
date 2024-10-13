package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.UserCredentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCredentialsRepository : JpaRepository<UserCredentials, Long> {
}
