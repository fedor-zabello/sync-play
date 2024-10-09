package org.pigletsinc.syncplay.user.repository

import org.pigletsinc.syncplay.user.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}