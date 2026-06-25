package org.pigletsinc.syncplay.repository.user.jpainterfaces

import org.pigletsinc.syncplay.repository.user.entity.UserCredentialsJpa
import org.springframework.data.jpa.repository.JpaRepository

interface UserCredentialsRepositoryJpa : JpaRepository<UserCredentialsJpa, Long> {
    fun findByEmailIgnoreCase(email: String): UserCredentialsJpa?
}
