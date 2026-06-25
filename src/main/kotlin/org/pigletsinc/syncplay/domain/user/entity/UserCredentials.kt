package org.pigletsinc.syncplay.domain.user.entity

data class UserCredentials(
    val id: Long? = null,
    val email: String,
    val password: String,
    val userProfile: UserProfile,
)
