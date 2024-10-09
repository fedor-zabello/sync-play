package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}