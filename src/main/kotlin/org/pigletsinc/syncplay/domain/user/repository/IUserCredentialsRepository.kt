package org.pigletsinc.syncplay.domain.user.repository

import org.pigletsinc.syncplay.domain.user.entity.UserCredentials

interface IUserCredentialsRepository {
    fun save(userCredentials: UserCredentials): UserCredentials
    fun findByEmailIgnoreCase(email: String): UserCredentials?
}
