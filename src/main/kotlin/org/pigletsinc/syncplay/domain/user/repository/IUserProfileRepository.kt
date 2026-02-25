package org.pigletsinc.syncplay.domain.user.repository

import org.pigletsinc.syncplay.domain.user.entity.UserProfile

interface IUserProfileRepository {
    fun save(userProfile: UserProfile): UserProfile
    fun findByName(username: String): UserProfile?
    fun findById(id: Long): UserProfile?
}
